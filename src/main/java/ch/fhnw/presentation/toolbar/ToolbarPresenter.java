package ch.fhnw.presentation.toolbar;

import ch.fhnw.business.movie.entity.Movie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    Label saveLbl;

    @FXML
    Label addLbl;

    @FXML
    Label removeLbl;

    @FXML
    TextField searchField;

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    private ObjectProperty<Movie> deletedMovie = new SimpleObjectProperty<>();

    private Optional<ToolbarActionsListener> toolbarListener = Optional.empty();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        removeLbl.disableProperty().bind(selectedMovie.isNull());

        removeLbl.setOnMouseClicked(event -> {
            deletedMovie.set(selectedMovie.get());
        });

        saveLbl.setOnMouseClicked(event -> {
            FileChooser fileChooser = new FileChooser();
            // Set extension filter
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV-files (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            // Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            toolbarListener
                    .orElseThrow(() -> new IllegalStateException("ToolbarListener must be set!"))
                    .onSave(file);

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
        void onSave(File file);
    }

}
