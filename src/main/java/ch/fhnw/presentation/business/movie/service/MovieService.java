package ch.fhnw.presentation.business.movie.service;

import ch.fhnw.presentation.business.movie.entity.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieService {

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

                        return movie;
                    })
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();

    }

}
