package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Ahoy! Unit tests for the MoviesController treasure hunting functionality, matey!
 */
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MockMovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services with pirate-worthy test data
        mockMovieService = new MockMovieService();
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection like a sneaky pirate
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should display all movies like showing off the treasure chest")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should be added to model");
        assertEquals(3, movies.size(), "Should have 3 test movies");
    }

    @Test
    @DisplayName("Should display movie details like examining a specific treasure")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movie-details", result, "Should return movie-details template");
        
        Movie movie = (Movie) model.getAttribute("movie");
        assertNotNull(movie, "Movie should be added to model");
        assertEquals("Test Movie", movie.getMovieName());
    }

    @Test
    @DisplayName("Should handle movie not found like a treasure that's already been plundered")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("error", result, "Should return error template");
        
        String title = (String) model.getAttribute("title");
        String message = (String) model.getAttribute("message");
        assertEquals("Movie Not Found", title);
        assertTrue(message.contains("999"), "Error message should contain the movie ID");
    }

    @Test
    @DisplayName("Should search movies by name like a pirate hunting for specific treasure")
    public void testSearchMoviesByName() {
        String result = moviesController.searchMovies("Test", null, null, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should be added to model");
        assertEquals(1, movies.size(), "Should find 1 movie with 'Test' in name");
        assertEquals("Test Movie", movies.get(0).getMovieName());
        
        // Verify search attributes
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search performed flag should be true");
        assertEquals("Test", model.getAttribute("searchName"));
        assertEquals(1, model.getAttribute("searchResultsCount"));
    }

    @Test
    @DisplayName("Should search movies by ID like finding treasure by map coordinates")
    public void testSearchMoviesById() {
        String result = moviesController.searchMovies(null, 2L, null, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should be added to model");
        assertEquals(1, movies.size(), "Should find 1 movie with ID 2");
        assertEquals(2L, movies.get(0).getId());
        
        // Verify search attributes
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search performed flag should be true");
        assertEquals(2L, model.getAttribute("searchId"));
        assertEquals(1, model.getAttribute("searchResultsCount"));
    }

    @Test
    @DisplayName("Should search movies by genre like sorting treasure by type")
    public void testSearchMoviesByGenre() {
        String result = moviesController.searchMovies(null, null, "Drama", model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should be added to model");
        assertEquals(2, movies.size(), "Should find 2 drama movies");
        
        // Verify search attributes
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search performed flag should be true");
        assertEquals("Drama", model.getAttribute("searchGenre"));
        assertEquals(2, model.getAttribute("searchResultsCount"));
    }

    @Test
    @DisplayName("Should handle empty search results like finding an empty treasure chest")
    public void testSearchMoviesNoResults() {
        String result = moviesController.searchMovies("NonExistent", null, null, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("movies", result, "Should return movies template");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertNotNull(movies, "Movies should be added to model");
        assertTrue(movies.isEmpty(), "Should have no movies in results");
        
        // Verify search attributes and no results message
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search performed flag should be true");
        assertEquals("NonExistent", model.getAttribute("searchName"));
        assertEquals(0, model.getAttribute("searchResultsCount"));
        assertNotNull(model.getAttribute("noResultsMessage"), "Should have no results message");
    }

    @Test
    @DisplayName("Should handle invalid search request like rejecting a fake treasure map")
    public void testSearchMoviesInvalidRequest() {
        String result = moviesController.searchMovies(null, null, null, model);
        assertNotNull(result, "Result should not be null!");
        assertEquals("error", result, "Should return error template");
        
        String title = (String) model.getAttribute("title");
        String message = (String) model.getAttribute("message");
        assertEquals("Search Error", title);
        assertTrue(message.contains("search criterion"), "Error message should mention search criteria");
        
        // Verify search attributes are preserved
        assertTrue((Boolean) model.getAttribute("searchPerformed"), "Search performed flag should be true");
    }

    @Test
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(3, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
    }

    /**
     * Mock MovieService for testing like a fake treasure map for practice
     */
    private static class MockMovieService extends MovieService {
        private final List<Movie> testMovies;

        public MockMovieService() {
            this.testMovies = Arrays.asList(
                new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0),
                new Movie(3L, "Drama Film", "Drama Director", 2021, "Drama", "Drama description", 130, 4.2)
            );
        }

        @Override
        public List<Movie> getAllMovies() {
            return testMovies;
        }
        
        @Override
        public Optional<Movie> getMovieById(Long id) {
            if (id == null || id <= 0) {
                return Optional.empty();
            }
            return testMovies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();
        }

        @Override
        public List<Movie> searchMovies(String name, Long id, String genre) {
            return testMovies.stream()
                .filter(movie -> {
                    if (id != null && !movie.getId().equals(id)) {
                        return false;
                    }
                    if (name != null && !name.trim().isEmpty()) {
                        String searchName = name.trim().toLowerCase();
                        String movieName = movie.getMovieName().toLowerCase();
                        if (!movieName.contains(searchName)) {
                            return false;
                        }
                    }
                    if (genre != null && !genre.trim().isEmpty()) {
                        String searchGenre = genre.trim().toLowerCase();
                        String movieGenre = movie.getGenre().toLowerCase();
                        if (!movieGenre.contains(searchGenre)) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(java.util.stream.Collectors.toList());
        }

        @Override
        public boolean isValidSearchRequest(String name, Long id, String genre) {
            boolean hasName = name != null && !name.trim().isEmpty();
            boolean hasId = id != null && id > 0;
            boolean hasGenre = genre != null && !genre.trim().isEmpty();
            
            if (!hasName && !hasId && !hasGenre) {
                return false;
            }
            
            if (id != null && id <= 0) {
                return false;
            }
            
            return true;
        }
    }
}
