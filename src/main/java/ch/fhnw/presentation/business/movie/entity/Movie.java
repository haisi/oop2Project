package ch.fhnw.presentation.business.movie.entity;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.util.Optional;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class Movie {

    final IntegerProperty id = new SimpleIntegerProperty();
    // TODO maybe status of entity (not saved and etc.)
    final StringProperty title = new SimpleStringProperty();
    final IntegerProperty yearOfAward = new SimpleIntegerProperty();
    final StringProperty director = new SimpleStringProperty();
    final StringProperty mainActor = new SimpleStringProperty();
    final StringProperty titleEnglish = new SimpleStringProperty();
    final IntegerProperty yearOfProduction = new SimpleIntegerProperty();
    //TODO List of string for countries (US/GB/...)
    final IntegerProperty duration = new SimpleIntegerProperty();
    final IntegerProperty fsk = new SimpleIntegerProperty();
    final StringProperty genre = new SimpleStringProperty();
    // In the CSV the startDate might be '-'
    final ObjectProperty<Optional<LocalDate>> startDate = new SimpleObjectProperty<>();
    final IntegerProperty numberOfOscars = new SimpleIntegerProperty();

    //TODO add constructor with all elements or builder-pattern (maybe lombdok to reduce boilerplate)

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        return !(id != null ? !id.equals(movie.id) : movie.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    //TODO property-getter for list of countries


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public int getYearOfAward() {
        return yearOfAward.get();
    }

    public IntegerProperty yearOfAwardProperty() {
        return yearOfAward;
    }

    public void setYearOfAward(int yearOfAward) {
        this.yearOfAward.set(yearOfAward);
    }

    public String getDirector() {
        return director.get();
    }

    public StringProperty directorProperty() {
        return director;
    }

    public void setDirector(String director) {
        this.director.set(director);
    }

    public String getMainActor() {
        return mainActor.get();
    }

    public StringProperty mainActorProperty() {
        return mainActor;
    }

    public void setMainActor(String mainActor) {
        this.mainActor.set(mainActor);
    }

    public String getTitleEnglish() {
        return titleEnglish.get();
    }

    public StringProperty titleEnglishProperty() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish.set(titleEnglish);
    }

    public int getYearOfProduction() {
        return yearOfProduction.get();
    }

    public IntegerProperty yearOfProductionProperty() {
        return yearOfProduction;
    }

    public void setYearOfProduction(int yearOfProduction) {
        this.yearOfProduction.set(yearOfProduction);
    }

    public int getDuration() {
        return duration.get();
    }

    public IntegerProperty durationProperty() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration.set(duration);
    }

    public int getFsk() {
        return fsk.get();
    }

    public IntegerProperty fskProperty() {
        return fsk;
    }

    public void setFsk(int fsk) {
        this.fsk.set(fsk);
    }

    public String getGenre() {
        return genre.get();
    }

    public StringProperty genreProperty() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre.set(genre);
    }

    public Optional<LocalDate> getStartDate() {
        return startDate.get();
    }

    public ObjectProperty<Optional<LocalDate>> startDateProperty() {
        return startDate;
    }

    public void setStartDate(Optional<LocalDate> startDate) {
        this.startDate.set(startDate);
    }

    public int getNumberOfOscars() {
        return numberOfOscars.get();
    }

    public IntegerProperty numberOfOscarsProperty() {
        return numberOfOscars;
    }

    public void setNumberOfOscars(int numberOfOscars) {
        this.numberOfOscars.set(numberOfOscars);
    }
}
