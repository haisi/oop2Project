package ch.fhnw.presentation.toolbar;

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
 *
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class ToolbarPresenter implements Initializable {

    @FXML
    Label addLbl;

    @FXML
    Label removeLbl;

    @FXML
    TextField searchField;

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    private ObjectProperty<Movie> deletedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        removeLbl.disableProperty().bind(selectedMovie.isNull());

        removeLbl.setOnMouseClicked(event -> {
            deletedMovie.set(selectedMovie.get());
        });

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public ObjectProperty<Movie> deletedMovieProperty() {
        return deletedMovie;
    }

}
