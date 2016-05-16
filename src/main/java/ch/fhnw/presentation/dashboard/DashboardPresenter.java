package ch.fhnw.presentation.dashboard;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
import ch.fhnw.presentation.movieEditor.MovieEditorPresenter;
import ch.fhnw.presentation.movieEditor.MovieEditorView;
import ch.fhnw.presentation.moviesTable.MoviesTablePresenter;
import ch.fhnw.presentation.moviesTable.MoviesTableView;
import ch.fhnw.presentation.toolbar.ToolbarPresenter;
import ch.fhnw.presentation.toolbar.ToolbarView;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.inject.Inject;

/**
 * @author hasan kara <hasan.kara@fhnw.ch>
 */
public class DashboardPresenter implements Initializable, ToolbarPresenter.ToolbarActionsListener {

    @FXML
    BorderPane borderPane;

    @FXML
    SplitPane splitPane;

    @Inject
    Stage primaryStage;

    @Inject
    MovieService movieService;

    private ToolbarView toolbarView;
    private ToolbarPresenter toolbarPresenter;

    private MoviesTableView moviesTableView;
    private MoviesTablePresenter moviesTablePresenter;

    private MovieEditorView movieEditorView;
    private MovieEditorPresenter movieEditorPresenter;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setToolbar();
        createMoviesTableView();
        createMovieEditor();

        // Somehow the binding doesn't work sometimes, so we have to use a change listener
//        movieEditorPresenter.selectedMovieProperty().bind(moviesTablePresenter.selectedMovieProperty());
        moviesTablePresenter.selectedMovieProperty().addListener((observable, oldValue, newValue) -> {
//            movieEditorPresenter.selectedMovieProperty().set(newValue);
            toolbarPresenter.selectedMovieProperty().set(newValue);
        });

        moviesTablePresenter.selectedMovieProperty().bindBidirectional(movieEditorPresenter.selectedMovieProperty());

        moviesTablePresenter.deletedMovieProperty().bind(toolbarPresenter.deletedMovieProperty());

        moviesTablePresenter.searchTextProperty().bind(toolbarPresenter.searchFieldTextProperty());

    }

    private void createMovieEditor() {
        movieEditorView = new MovieEditorView();
        ScrollPane scrollPane = new ScrollPane(movieEditorView.getView());
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        movieEditorPresenter = (MovieEditorPresenter) movieEditorView.getPresenter();

        splitPane.getItems().add(scrollPane);
    }

    private void createMoviesTableView() {
        moviesTableView = new MoviesTableView();
        moviesTablePresenter = (MoviesTablePresenter) moviesTableView.getPresenter();
        splitPane.getItems().add(moviesTableView.getView());
    }

    private void setToolbar() {
        toolbarView = new ToolbarView();
        toolbarPresenter = (ToolbarPresenter) toolbarView.getPresenter();
        toolbarPresenter.setToolbarListener(DashboardPresenter.this);
        // TODO use toolbarPresenter to bind textfield-value to table
        // TODO add listener-object to toolbarPresenter for remove and add item
        borderPane.setTop(toolbarView.getView());
    }

    @Override
    public void onSave(File file) {
        ObservableList<Movie> movies = moviesTablePresenter.getData();
        try {
            movieService.saveMovies(file, movies);
            showSuccessfulSavedNotification(file);
        } catch (IOException e) {
            showSavingFailedNotification(file);
            e.printStackTrace();
        }
    }

    private void showSuccessfulSavedNotification(final File file) {
        Notifications.create()
                .title("Saved file")
                .text(file.getAbsolutePath())
                .owner(primaryStage)
                .hideAfter(Duration.seconds(3))
                .position(Pos.BOTTOM_RIGHT)
                .onAction(event -> System.out.println("Notification clicked on!")) //TODO open file on click
                .showConfirm();
    }

    private void showSavingFailedNotification(final File file) {
        Notifications.create()
                .title("Failed to save file!")
                .text(file.getAbsolutePath())
                .owner(primaryStage)
                .hideAfter(Duration.seconds(5))
                .position(Pos.BOTTOM_RIGHT)
                .showError();
    }

    @Override
    public void onAddNewMovie() {
        moviesTablePresenter.addNewMovie();
    }

    @Override
    public void onChangeLanguage() {
        if (Locale.getDefault().equals(Locale.GERMAN)) {
            Locale.setDefault(Locale.ENGLISH);
        } else {
            Locale.setDefault(Locale.GERMAN);
        }

        DashboardView dashboardView = new DashboardView();
        Parent newDashBoardViewInstance = dashboardView.getView();

        // replace old view-instance with new one
        AnchorPane root = (AnchorPane) primaryStage.getScene().getRoot();
        ObservableList<Node> children = root.getChildren();
        children.clear();
        children.add(newDashBoardViewInstance);

        // set anchors to the edge of stage
        root = (AnchorPane) primaryStage.getScene().getRoot();

        Node node = root.getChildren().get(0);
        AnchorPane.setBottomAnchor(node, 0d);
        AnchorPane.setTopAnchor(node, 0d);
        AnchorPane.setLeftAnchor(node, 0d);
        AnchorPane.setRightAnchor(node, 0d);

    }
}
