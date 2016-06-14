package ch.fhnw.presentation.movieEditor;

import ch.fhnw.business.movie.entity.Movie;
import com.google.common.base.Joiner;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import org.controlsfx.control.MaskerPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieEditorPresenter implements Initializable {

    @FXML
    StackPane root;

    private MaskerPane masker = new MaskerPane();

    @FXML
    Label yearOfAwardLabel;
    @FXML
    Label titleLabel;
    @FXML
    FlowPane countriesPane;
    @FXML
    Label directorLabel;
    @FXML
    Label mainActorLabel;
    @FXML
    ImageView posterImage;
    @FXML
    FlowPane oscarsPane;

    @FXML
    TextField mainActorField;
    @FXML
    TextField englishTitleField;

    @FXML
    Spinner<Integer> yearSpinner;
    @FXML
    TextField titleField;
    @FXML
    TextField directorField;
    @FXML
    TextField genreField;
    @FXML
    TextField countriesField;
    @FXML
    Spinner<Integer> oscarsSpinner;

    @FXML
    Spinner<Integer> yearOfProductionSpinner;
    @FXML
    Spinner<Integer> durationSpinner;
    @FXML
    DatePicker startDatePicker;
    @FXML
    ComboBox<Integer> fskComboBox;
    ObservableList<Integer> fskOptions = FXCollections.observableArrayList(0, 6, 12, 16, 18);

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    private void addBinding(TextField textField, MovieStringAttributeSetter setter) {

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Movie movie = selectedMovie.get();
                setter.setString(movie, newValue);
                selectedMovie.set(movie);
            }
        });
    }

    private void addBinding(Spinner<Integer> spinner, MovieIntegerAttributeSetter setter) {

        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && selectedMovie.isNotNull().get()) {
                Movie movie = selectedMovie.get();
                setter.setInteger(movie, newValue);
                selectedMovie.set(movie);
            }
        });
    }

    private void addBinding(ComboBox<Integer> comboBox, MovieIntegerAttributeSetter setter) {

        comboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Movie movie = selectedMovie.get();
                setter.setInteger(movie, newValue);
                selectedMovie.set(movie);
            }
        });
    }

    private void addBinding(DatePicker datePicker, MovieLocalDateAttributeSetter setter) {

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            Movie movie = selectedMovie.get();
            setter.setLocalDate(movie, newValue);
            selectedMovie.set(movie);
        });
    }


    @FunctionalInterface
    public interface MovieStringAttributeSetter {
        void setString(Movie movie, String newValue);
    }

    @FunctionalInterface
    public interface MovieIntegerAttributeSetter {
        void setInteger(Movie movie, Integer newValue);
    }

    @FunctionalInterface
    public interface MovieLocalDateAttributeSetter {
        void setLocalDate(Movie movie, LocalDate newValue);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addBinding(titleField, Movie::setTitle);
        addBinding(englishTitleField, Movie::setTitleEnglish);
        addBinding(directorField, Movie::setDirector);
        addBinding(mainActorField, Movie::setMainActor);
        addBinding(genreField, Movie::setGenre);
        addBinding(countriesField, (movie, newValue) -> {
            String[] countries = newValue.split("/");
            movie.setCountry(FXCollections.observableArrayList(countries));
        });

        addBinding(yearOfProductionSpinner, Movie::setYearOfProduction);
        addBinding(yearSpinner, Movie::setYearOfAward);
        addBinding(oscarsSpinner, Movie::setNumberOfOscars);
        addBinding(durationSpinner, Movie::setDuration);

        addBinding(fskComboBox, Movie::setFsk);

        addBinding(startDatePicker, (movie, newStartDate) -> {
                movie.setStartDate(Optional.ofNullable(newStartDate));
        });

        root.getChildren().addAll(masker);
        masker.setVisible(true);
        masker.setProgressVisible(false);
        masker.setText(resources.getString("no_movie_selected"));

        clearEditor();

        titleLabel.textProperty().bind(titleField.textProperty());
        directorLabel.textProperty().bind(directorField.textProperty());
        mainActorLabel.textProperty().bind(mainActorField.textProperty());
        yearOfAwardLabel.textProperty().bind(yearSpinner.valueProperty().asString());

        fskComboBox.setItems(fskOptions);
        fskComboBox.setButtonCell(new FskListCell());
        fskComboBox.setCellFactory(param -> new FskListCell());

        countriesField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                setCountriesFlags(selectedMovie.getValue());
            }
        });

        oscarsSpinner.valueProperty().addListener((observable, oldValue, newNumberOfOscars) -> {

            ObservableList<Node> children = oscarsPane.getChildren();
            children.clear();

            String imageUrl = getClass().getResource("/Oscar-logo.png").toExternalForm();
            Image oscarImage = new Image(imageUrl);

            for (int i = 0; i < newNumberOfOscars; i++) {
                ImageView imageView = new ImageView(oscarImage);
                imageView.setFitHeight(40);

                imageView.setPreserveRatio(true);

                children.add(imageView);
            }
        });

        selectedMovie.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                root.setDisable(false);
                masker.setVisible(false);

                URL resource = getClass().getResource("/posters/" + newValue.getId() + ".jpg");
                if (resource != null) {
                    String imageUrl = resource.toExternalForm();
                    posterImage.setImage(new Image(imageUrl));
                } else {
                    posterImage.setImage(null);
                }

                setCountriesFlags(newValue);

                yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2099, newValue.getYearOfAward()));
                titleField.setText(newValue.getTitle());
                directorField.setText(newValue.getDirector());
                mainActorField.setText(newValue.getMainActor());
                englishTitleField.setText(newValue.getTitleEnglish());
                genreField.setText(newValue.getGenre());

                String countriesJoined = Joiner.on("/").join(newValue.getCountry().toArray());
                countriesField.setText(countriesJoined);

                yearOfProductionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2099, newValue.getYearOfProduction()));
                durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 700, newValue.getDuration()));
                startDatePicker.setValue(newValue.startDateProperty().get().orElse(null));

                fskComboBox.setValue(newValue.getFsk());

                oscarsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, newValue.getNumberOfOscars()));

            } else {

                root.setDisable(true);
                masker.setVisible(true);
                clearEditor();

            }
        });

    }

    private void clearEditor() {
//        titleLabel.setText("empty");
//        directorLabel.setText("empty");
//        mainActorLabel.setText("empty");
//        yearOfAwardLabel.setText("empty");

        oscarsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        yearOfProductionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 0, 0));
        startDatePicker.setValue(null);

        posterImage.setImage(null);
        countriesPane.getChildren().clear();
        oscarsPane.getChildren().clear();

        fskComboBox.setValue(null);

        countriesField.setText(null);
        genreField.setText(null);
        englishTitleField.setText(null);
        mainActorField.setText(null);
        directorField.setText(null);
        titleField.setText(null);
    }

    private void setCountriesFlags(Movie newValue) {

        final ObservableList<Node> children = countriesPane.getChildren();
        children.clear();

        newValue.getCountry().stream()
                .distinct()
                .map(s -> getClass().getResource("/flags/" + s.toLowerCase() + ".png"))
                .filter(Objects::nonNull)
                .map(resource -> {
                    String imageUrl = resource.toExternalForm();
                    return new ImageView(imageUrl);
                })
                .forEach(children::add);

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

}
