package ch.fhnw.business.movie.entity;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * @author Hasan Kara <hasan.kara@fhnw.ch>
 */
public class MovieTest {

    @Test
    public void testToCsvRow() throws Exception {
        String expected = "47;Rocky;1977;John G. Avildsen;Sylvester Stallone, Talia Shire;Rocky;1976;US;118;12;Drama, Sportfilm;01.04.1977;1";

        ArrayList<String> countries = new ArrayList<>();
        countries.add("US");

        Movie actualMovie = new Movie(47, "Rocky", 1977, "John G. Avildsen", "Sylvester Stallone, Talia Shire", "Rocky",
                                        1976, countries, 118, 12, "Drama, Sportfilm", LocalDate.of(1977, 4, 1), 1);

        String actualCsv = actualMovie.toCsvRow();

        assertEquals("Created csv row is expected", actualCsv, expected);

    }

    @Test
    public void testToCsvRowWithEmptyDate() throws Exception {
        String expected = "47;Rocky;1977;John G. Avildsen;Sylvester Stallone, Talia Shire;Rocky;1976;US;118;12;Drama, Sportfilm;-;1";

        ArrayList<String> countries = new ArrayList<>();
        countries.add("US");

        Movie actualMovie = new Movie(47, "Rocky", 1977, "John G. Avildsen", "Sylvester Stallone, Talia Shire", "Rocky",
                1976, countries, 118, 12, "Drama, Sportfilm", 1);

        String actualCsv = actualMovie.toCsvRow();

        assertEquals("Created csv row is expected", actualCsv, expected);

    }

    @Test
    public void testToCsvRowWithMultipleCountries() throws Exception {
        String expected = "47;Rocky;1977;John G. Avildsen;Sylvester Stallone, Talia Shire;Rocky;1976;US/DE;118;12;Drama, Sportfilm;-;1";

        ArrayList<String> countries = new ArrayList<>();
        countries.add("US");
        countries.add("DE");

        Movie actualMovie = new Movie(47, "Rocky", 1977, "John G. Avildsen", "Sylvester Stallone, Talia Shire", "Rocky",
                1976, countries, 118, 12, "Drama, Sportfilm", 1);

        String actualCsv = actualMovie.toCsvRow();

        assertEquals("Created csv row is expected", actualCsv, expected);

    }


}