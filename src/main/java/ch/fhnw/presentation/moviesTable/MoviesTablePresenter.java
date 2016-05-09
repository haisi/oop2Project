package ch.fhnw.presentation.moviesTable;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;

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

    private StringProperty searchText = new SimpleStringProperty();
    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();
    private ObjectProperty<Movie> deletedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Movie> allMovies = movieService.getAllMovies();
        data.addAll(allMovies);

        FilteredList<Movie> filteredData = new FilteredList<>(data, p -> true);

        searchText.addListener((observable, oldValue, searchValue) -> {

            filteredData.setPredicate(movie -> {
                // If filter text is empty, display all persons.
                if (searchValue == null || searchValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = searchValue.toLowerCase();

                if (movie.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches title
                } else if (movie.getTitleEnglish().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches english title
                }
                return false; // Does not match.
            });

        });

        // Wrap the FilteredList in a SortedList.
        SortedList<Movie> sortedData = new SortedList<>(filteredData);

        // Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(moviesTable.comparatorProperty());

        moviesTable.setItems(sortedData);

        // Use listener to set an ObjectProperty, instead of binding directly to selectedItemProperty
        // because the selectedItemProperty is a read only object.
        this.moviesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedMovie.set(newValue);
        });

        deletedMovie.addListener((observable, oldValue, newValue) -> {
            data.remove(newValue);
        });

        setCellValueFactories();

        // Align text in year column to right
        yearColumn.setCellFactory(cell -> new TableCell<Movie, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setTextAlignment(TextAlignment.RIGHT);
                    setAlignment(Pos.CENTER_RIGHT);
                    setText(String.valueOf(item));
                }

            }
        });

    }

    private void setCellValueFactories() {

        // TODO: set factory for status
        yearColumn.setCellValueFactory(cellData -> cellData.getValue().yearOfAwardProperty());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        directorColumn.setCellValueFactory(cellData -> cellData.getValue().directorProperty());
        mainActorColumn.setCellValueFactory(cellData -> cellData.getValue().mainActorProperty());

    }

    public StringProperty searchTextProperty() {
        return searchText;
    }


    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

    public ObjectProperty<Movie> deletedMovieProperty() {
        return deletedMovie;
    }

    /**
     * @return ObservableList of movies
     */
    public ObservableList<Movie> getData() {
        return data;
    }
}
