package ch.fhnw.presentation.movieEditor;

import ch.fhnw.business.movie.entity.Movie;
import com.google.common.base.Joiner;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.controlsfx.control.MaskerPane;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

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
    //TODO find proper type for fsk
    @FXML
    ComboBox<Integer> fskComboBox;
    ObservableList<Integer> fskOptions = FXCollections.observableArrayList(0, 6, 12, 16, 18);

    private ObjectProperty<Movie> selectedMovie = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        root.getChildren().addAll(masker);
        masker.setVisible(true);
        masker.setProgressVisible(false);
        masker.setText("Select a movie");

        clearEditor();

        fskComboBox.setItems(fskOptions);
        fskComboBox.setButtonCell(new FskListCell());
        fskComboBox.setCellFactory(param -> new FskListCell());

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

                masker.setVisible(false);

                // TODO: handle FileNotFoundException
                String imageUrl = getClass().getResource("/posters/" + newValue.getId() + ".jpg").toExternalForm();
                posterImage.setImage(new Image(imageUrl));

                setCountriesFlags(newValue);

                yearOfAwardLabel.setText(String.valueOf(newValue.getYearOfAward()));
                titleLabel.setText(newValue.getTitle());
                directorLabel.setText("Von " + newValue.getDirector());
                mainActorLabel.setText("Mit " + newValue.getMainActor());

                // TODO: extract max-values into config.file
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

                masker.setVisible(true);
                clearEditor();

            }
        });

    }

    private void clearEditor() {
        titleLabel.setText("empty");
        directorLabel.setText("empty");
        mainActorLabel.setText("empty");
        yearOfAwardLabel.setText("empty");

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
        // TODO: handle FileNotFoundException
        newValue.getCountry().stream().map(s -> {
            String imageUrl = getClass().getResource("/flags/" + s.toLowerCase() + ".png").toExternalForm();
            return new ImageView(imageUrl);
        }).forEach(children::add);

    }

    public ObjectProperty<Movie> selectedMovieProperty() {
        return selectedMovie;
    }

}
