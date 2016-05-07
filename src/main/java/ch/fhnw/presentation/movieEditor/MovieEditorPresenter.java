package ch.fhnw.presentation.movieEditor;

import ch.fhnw.business.movie.entity.Movie;
import com.google.common.base.Joiner;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieEditorPresenter implements Initializable {

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
    //TODO find proper type for fsk
    @FXML
    ComboBox<Integer> fskComboBox;
    ObservableList<Integer> fskOptions = FXCollections.observableArrayList(0, 6, 12, 16, 18);

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // TODO: extract max-values into config.file
        yearSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2099, LocalDate.now().getYear()));
        yearOfProductionSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 2099, LocalDate.now().getYear()));
        durationSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 700, 0));
        oscarsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 20, 0));

        fskComboBox.setItems(fskOptions);
        fskComboBox.setButtonCell(new FskListCell());
        fskComboBox.setCellFactory(param -> new FskListCell());

        selectedMovie.addListener((observable, oldValue, newValue) -> {

            if (newValue != null) {

                // TODO: handle FileNotFoundException
                String imageUrl = getClass().getResource("/posters/" + newValue.getId() + ".jpg").toExternalForm();
                posterImage.setImage(new Image(imageUrl));

                yearOfAwardLabel.setText(String.valueOf(newValue.getYearOfAward()));
                titleLabel.setText(newValue.getTitle());
                directorLabel.setText("Von " + newValue.getDirector());
                mainActorLabel.setText("Mit " + newValue.getMainActor());

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
                // TODO: disable buttons
                titleField.setText("empty");
            }
        });

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

}
