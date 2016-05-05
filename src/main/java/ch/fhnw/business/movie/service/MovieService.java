package ch.fhnw.business.movie.service;

import ch.fhnw.business.movie.entity.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieService {

    final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<Movie> getAllMovies() {

        InputStream resourceAsStream = MovieService.class.getClass().getResourceAsStream("/movies.csv");

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(resourceAsStream))) {
            return buffer
                    .lines()
                    .skip(1)
                    .map(s -> s.split(";"))
                    .map(strings -> {
                        Movie movie = new Movie();

                        movie.setId(Integer.parseInt(strings[0]));
                        movie.setTitle(strings[1]);
                        movie.setYearOfAward(Integer.parseInt(strings[2]));
                        movie.setDirector(strings[3]);
                        movie.setMainActor(strings[4]);
                        movie.setTitleEnglish(strings[5]);
                        movie.setYearOfProduction(Integer.parseInt(strings[6]));
                        movie.addCountries(strings[7].split("/"));
                        movie.setDuration(Integer.parseInt(strings[8]));
                        movie.setDuration(Integer.parseInt(strings[9]));
                        movie.setGenre(strings[10]);

                        try {
                            LocalDate startDate = LocalDate.parse(strings[11], dtf);
                            movie.setStartDate(Optional.of(startDate));
                        } catch (DateTimeParseException ex) {
                            movie.setStartDate(Optional.empty());
                        }

                        movie.setNumberOfOscars(Integer.parseInt(strings[12]));

                        return movie;
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();

    }

}