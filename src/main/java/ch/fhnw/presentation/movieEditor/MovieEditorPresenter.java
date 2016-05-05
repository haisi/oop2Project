package ch.fhnw.presentation.movieEditor;

import ch.fhnw.business.movie.entity.Movie;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieEditorPresenter implements Initializable {

    @FXML
    TextField title;

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        selectedMovie.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {
                title.setText(newValue.getTitle());
            } else {
                // TODO: disable buttons
                title.setText("empty");
            }
        });

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

}
