package ch.fhnw.presentation.moviesTable;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MoviesTablePresenter implements Initializable {

    @Inject
    MovieService movieService;

    private final ObservableList<Movie> data = FXCollections.observableArrayList();

    @FXML
    AnchorPane anchorPane;

    @FXML
    TableView<Movie> moviesTable;
    // TODO: find type for status
    @FXML
    TableColumn<Movie, Object> statusColumn;
    @FXML
    TableColumn<Movie, Number> yearColumn;
    @FXML
    TableColumn<Movie, String> titleColumn;
    @FXML
    TableColumn<Movie, String> directorColumn;
    @FXML
    TableColumn<Movie, String> mainActorColumn;

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();
    private ObjectProperty<Movie> deletedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Movie> allMovies = movieService.getAllMovies();
        data.addAll(allMovies);
        moviesTable.setItems(data);

        // Use listener to set an ObjectProperty, instead of binding directly to selectedItemProperty
        // because the selectedItemProperty is a read only object.
        this.moviesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMovie.set(newValue);
        });

        deletedMovie.addListener((observable, oldValue, newValue) -> {
            data.remove(newValue);
        });

        setCellValueFactories();

    }

    private void setCellValueFactories() {

        // TODO: set factory for status
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearOfAwardProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        directorColumn.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        mainActorColumn.setCellValueFactory(cellData -> cellData.getValue().mainActorProperty());

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public ObjectProperty<Movie> deletedMovieProperty() {
        return deletedMovie;
    }

}
