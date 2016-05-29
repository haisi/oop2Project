package ch.fhnw.presentation.toolbar;

import ch.fhnw.business.movie.entity.Movie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.inject.Inject;
import java.io.File;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class ToolbarPresenter implements Initializable {

    @Inject
    Stage primaryStage;

    @FXML
    Button saveBtn;

    @FXML
    Button addBtn;

    @FXML
    Button removeBtn;

    @FXML
    Label changeLangLbl;

    @FXML
    TextField searchField;

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    private ObjectProperty<Movie> deletedMovie = new SimpleObjectProperty<>();

    private Optional<ToolbarActionsListener> toolbarListener = Optional.empty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addBtn.setOnMouseClicked(event -> {
            toolbarListener
                    .orElseThrow(() -> new IllegalStateException("ToolbarListener must be set!"))
                    .onAddNewMovie();
        });

        removeBtn.disableProperty().bind(selectedMovie.isNull());

        removeBtn.setOnMouseClicked(event -> {
            deletedMovie.set(selectedMovie.get());
        });

        saveBtn.setOnMouseClicked(event -> {

            toolbarListener
                    .orElseThrow(() -> new IllegalStateException("ToolbarListener must be set!"))
                    .onSave();

        });

        changeLangLbl.setOnMouseClicked(event -> {
            toolbarListener
                    .orElseThrow(() -> new IllegalStateException("ToolbarListener must be set!"))
                    .onChangeLanguage();

        });

    }

    public StringProperty searchFieldTextProperty() {
        return searchField.textProperty();
    }

    /**
     *
     * @param toolbarListener must not be null
     */
    public void setToolbarListener(ToolbarActionsListener toolbarListener) {
        this.toolbarListener = Optional.of(toolbarListener);
    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public ObjectProperty<Movie> deletedMovieProperty() {
        return deletedMovie;
    }

    public interface ToolbarActionsListener {
        void onSave();
        void onAddNewMovie();
        void onChangeLanguage();
    }

}
