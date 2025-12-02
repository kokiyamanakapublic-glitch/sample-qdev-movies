package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Ahoy! Unit tests for the MovieService treasure hunting functionality, matey!
 */
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should load movies from JSON like a proper pirate loading treasure")
    public void testGetAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        assertNotNull(movies, "Movies list should not be null, ye scallywag!");
        assertFalse(movies.isEmpty(), "Should have some treasure in the chest!");
        
        // Verify we have the expected movies from the JSON
        assertTrue(movies.size() >= 12, "Should have at least 12 movies in the treasure chest");
    }

    @Test
    @DisplayName("Should find movie by ID like a skilled treasure hunter")
    public void testGetMovieById() {
        Optional<Movie> movie = movieService.getMovieById(1L);
        assertTrue(movie.isPresent(), "Should find the treasure with ID 1!");
        assertEquals("The Prison Escape", movie.get().getMovieName());
        assertEquals("Drama", movie.get().getGenre());
    }

    @Test
    @DisplayName("Should return empty when searching for non-existent movie ID")
    public void testGetMovieByIdNotFound() {
        Optional<Movie> movie = movieService.getMovieById(999L);
        assertFalse(movie.isPresent(), "Should not find treasure that doesn't exist!");
    }

    @Test
    @DisplayName("Should return empty for invalid movie ID")
    public void testGetMovieByIdInvalid() {
        Optional<Movie> movie1 = movieService.getMovieById(null);
        Optional<Movie> movie2 = movieService.getMovieById(0L);
        Optional<Movie> movie3 = movieService.getMovieById(-1L);
        
        assertFalse(movie1.isPresent(), "Should not find treasure with null ID!");
        assertFalse(movie2.isPresent(), "Should not find treasure with zero ID!");
        assertFalse(movie3.isPresent(), "Should not find treasure with negative ID!");
    }

    @Test
    @DisplayName("Should search movies by name like a savvy pirate")
    public void testSearchMoviesByName() {
        List<Movie> results = movieService.searchMovies("Prison", null, null);
        assertNotNull(results, "Search results should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie with 'Prison' in the name");
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should search movies by name case-insensitively")
    public void testSearchMoviesByNameCaseInsensitive() {
        List<Movie> results1 = movieService.searchMovies("prison", null, null);
        List<Movie> results2 = movieService.searchMovies("PRISON", null, null);
        List<Movie> results3 = movieService.searchMovies("Prison", null, null);
        
        assertEquals(1, results1.size(), "Should find movie with lowercase search");
        assertEquals(1, results2.size(), "Should find movie with uppercase search");
        assertEquals(1, results3.size(), "Should find movie with mixed case search");
        
        assertEquals(results1.get(0).getId(), results2.get(0).getId());
        assertEquals(results2.get(0).getId(), results3.get(0).getId());
    }

    @Test
    @DisplayName("Should search movies by ID like finding specific treasure")
    public void testSearchMoviesById() {
        List<Movie> results = movieService.searchMovies(null, 1L, null);
        assertNotNull(results, "Search results should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie with ID 1");
        assertEquals(1L, results.get(0).getId());
        assertEquals("The Prison Escape", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should search movies by genre like sorting treasure by type")
    public void testSearchMoviesByGenre() {
        List<Movie> results = movieService.searchMovies(null, null, "Drama");
        assertNotNull(results, "Search results should not be null!");
        assertTrue(results.size() > 0, "Should find at least one drama movie");
        
        // Verify all results contain "Drama" in genre
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"), 
                      "All results should contain 'drama' in genre: " + movie.getGenre());
        }
    }

    @Test
    @DisplayName("Should search movies by genre case-insensitively")
    public void testSearchMoviesByGenreCaseInsensitive() {
        List<Movie> results1 = movieService.searchMovies(null, null, "drama");
        List<Movie> results2 = movieService.searchMovies(null, null, "DRAMA");
        List<Movie> results3 = movieService.searchMovies(null, null, "Drama");
        
        assertTrue(results1.size() > 0, "Should find movies with lowercase genre search");
        assertEquals(results1.size(), results2.size(), "Case should not matter for genre search");
        assertEquals(results2.size(), results3.size(), "Case should not matter for genre search");
    }

    @Test
    @DisplayName("Should search with multiple criteria like a thorough treasure hunter")
    public void testSearchMoviesMultipleCriteria() {
        // Search for Drama movies with "The" in the name
        List<Movie> results = movieService.searchMovies("The", null, "Drama");
        assertNotNull(results, "Search results should not be null!");
        assertTrue(results.size() > 0, "Should find movies matching both criteria");
        
        // Verify all results match both criteria
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"), 
                      "Movie name should contain 'the': " + movie.getMovieName());
            assertTrue(movie.getGenre().toLowerCase().contains("drama"), 
                      "Movie genre should contain 'drama': " + movie.getGenre());
        }
    }

    @Test
    @DisplayName("Should return empty list when no movies match search criteria")
    public void testSearchMoviesNoResults() {
        List<Movie> results = movieService.searchMovies("NonExistentMovie", null, null);
        assertNotNull(results, "Search results should not be null!");
        assertTrue(results.isEmpty(), "Should return empty list when no matches found");
    }

    @Test
    @DisplayName("Should validate search requests like a careful pirate checking the map")
    public void testIsValidSearchRequest() {
        // Valid requests
        assertTrue(movieService.isValidSearchRequest("Prison", null, null), 
                  "Should be valid with name only");
        assertTrue(movieService.isValidSearchRequest(null, 1L, null), 
                  "Should be valid with ID only");
        assertTrue(movieService.isValidSearchRequest(null, null, "Drama"), 
                  "Should be valid with genre only");
        assertTrue(movieService.isValidSearchRequest("Prison", 1L, "Drama"), 
                  "Should be valid with all criteria");
        
        // Invalid requests
        assertFalse(movieService.isValidSearchRequest(null, null, null), 
                   "Should be invalid with no criteria");
        assertFalse(movieService.isValidSearchRequest("", null, null), 
                   "Should be invalid with empty name");
        assertFalse(movieService.isValidSearchRequest("   ", null, null), 
                   "Should be invalid with whitespace-only name");
        assertFalse(movieService.isValidSearchRequest(null, 0L, null), 
                   "Should be invalid with zero ID");
        assertFalse(movieService.isValidSearchRequest(null, -1L, null), 
                   "Should be invalid with negative ID");
        assertFalse(movieService.isValidSearchRequest(null, null, ""), 
                   "Should be invalid with empty genre");
        assertFalse(movieService.isValidSearchRequest(null, null, "   "), 
                   "Should be invalid with whitespace-only genre");
    }

    @Test
    @DisplayName("Should handle partial matches for movie names")
    public void testSearchMoviesPartialNameMatch() {
        List<Movie> results = movieService.searchMovies("Family", null, null);
        assertNotNull(results, "Search results should not be null!");
        assertEquals(1, results.size(), "Should find 'The Family Boss' movie");
        assertEquals("The Family Boss", results.get(0).getMovieName());
    }

    @Test
    @DisplayName("Should handle partial matches for genres")
    public void testSearchMoviesPartialGenreMatch() {
        List<Movie> results = movieService.searchMovies(null, null, "Crime");
        assertNotNull(results, "Search results should not be null!");
        assertTrue(results.size() > 0, "Should find movies with Crime genre");
        
        // Verify all results contain "Crime" in genre (including "Crime/Drama")
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("crime"), 
                      "All results should contain 'crime' in genre: " + movie.getGenre());
        }
    }

    @Test
    @DisplayName("Should handle whitespace in search parameters")
    public void testSearchMoviesWithWhitespace() {
        List<Movie> results1 = movieService.searchMovies("  Prison  ", null, null);
        List<Movie> results2 = movieService.searchMovies(null, null, "  Drama  ");
        
        assertEquals(1, results1.size(), "Should handle whitespace in name search");
        assertTrue(results2.size() > 0, "Should handle whitespace in genre search");
    }
}