package ch.fhnw.presentation.dashboard;


import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
import ch.fhnw.presentation.movieEditor.MovieEditorPresenter;
import ch.fhnw.presentation.movieEditor.MovieEditorView;
import ch.fhnw.presentation.moviesTable.MoviesTablePresenter;
import ch.fhnw.presentation.moviesTable.MoviesTableView;
import ch.fhnw.presentation.toolbar.ToolbarPresenter;
import ch.fhnw.presentation.toolbar.ToolbarView;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

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
            movieEditorPresenter.selectedMovieProperty().set(newValue);
            toolbarPresenter.selectedMovieProperty().set(newValue);
        });

        moviesTablePresenter.deletedMovieProperty().bind(toolbarPresenter.deletedMovieProperty());

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
        movieService.saveMovies(file, movies);
    }
}
