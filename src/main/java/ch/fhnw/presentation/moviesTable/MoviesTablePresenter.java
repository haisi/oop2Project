package ch.fhnw.presentation.moviesTable;

import ch.fhnw.business.movie.entity.Movie;
import ch.fhnw.business.movie.service.MovieService;
import ch.fhnw.presentation.util.LevenshteinDistance;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MoviesTablePresenter implements Initializable {

    public static final int MAX_DISTANCE = 10;

    @Inject
    MovieService movieService;

    @Inject
    Stage primaryStage;

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
    private SortedList<Movie> sortedData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        movieService
                .getAllMovies()
                .whenComplete((movies, throwable) -> Platform.runLater(() -> {
                    if (throwable == null) {
                        data.addAll(movies);
                    } else {
                        Notifications.create()
                                .title("Failed to load movies!")
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.BOTTOM_RIGHT)
                                .owner(primaryStage)
                                .showError();
                    }
                }));

        FilteredList<Movie> filteredData = new FilteredList<>(data, p -> true);

        searchText.addListener((observable, oldValue, searchValue) -> {

            filteredData.setPredicate(movie -> {
                // If filter text is empty, display all persons.
                if (searchValue == null || searchValue.isEmpty()) {
                    // By default sort ASC by year of award
                    sortedData.setComparator((o1, o2) -> Integer.compare(o1.getYearOfAward(), o2.getYearOfAward()));
                    return true;
                }

                String lowerCaseFilter = searchValue.toLowerCase();

                int titleDistance = LevenshteinDistance.computeLevenshteinDistance(movie.getTitle().toLowerCase(), lowerCaseFilter);
                int englishTitleDistance = LevenshteinDistance.computeLevenshteinDistance(movie.getTitleEnglish().toLowerCase(), lowerCaseFilter);

                // Sort filtered data by ASC Levenshtein distance
                sortedData.setComparator((o1, o2) -> {
                    int o1Distance = LevenshteinDistance.computeLevenshteinDistance(o1.getTitle().toLowerCase(), lowerCaseFilter);
                    int o2Distance = LevenshteinDistance.computeLevenshteinDistance(o2.getTitle().toLowerCase(), lowerCaseFilter);
                    return Integer.compare(o1Distance, o2Distance);
                });

                // Only display titles with low Levenshtein distance
                return titleDistance < MAX_DISTANCE || englishTitleDistance < MAX_DISTANCE;
            });

        });

        // Wrap the FilteredList in a SortedList.
        sortedData = new SortedList<>(filteredData);

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

    public Movie addNewMovie() {
        Movie newEmptyMovie = movieService.createNewEmptyMovie();

        data.add(0, newEmptyMovie);
        moviesTable.scrollTo(0);
        moviesTable.getSelectionModel().select(0);

        return newEmptyMovie;
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
