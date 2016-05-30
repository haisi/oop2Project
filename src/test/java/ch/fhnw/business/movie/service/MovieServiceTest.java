package ch.fhnw.business.movie.service;

import ch.fhnw.business.movie.entity.Movie;
import org.junit.Test;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieServiceTest {

    @Test
    public void testMovieExtraction() throws Exception {
        String[] input = {"13","Mrs. Miniver","1943","William Wyler","Greer Garson, Walter Pidgeon","Mrs. Miniver","1942","US","134","12","Drama","21.10.1960","1"};

        Movie expected = new Movie(13, "Mrs. Miniver", 1943, "William Wyler", "Greer Garson, Walter Pidgeon", "Mrs. Miniver", 1942, Arrays.asList("US"), 134, 12, "Drama", LocalDate.of(1960, 10, 21), 1);
        Movie actualMovie = new MovieService().extractMovie(input);

        assertTrue(expected.deepEquals(actualMovie));
    }
}