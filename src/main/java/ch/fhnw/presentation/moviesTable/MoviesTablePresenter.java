package ch.fhnw.presentation.moviesTable;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setCellValueFactories();

        List<Movie> allMovies = movieService.getAllMovies();
        data.addAll(allMovies);
        moviesTable.setItems(data);

    }

    private void setCellValueFactories() {

        // TODO: set factory for status
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearOfAwardProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        directorColumn.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        mainActorColumn.setCellValueFactory(cellData -> cellData.getValue().mainActorProperty());

    }
}
