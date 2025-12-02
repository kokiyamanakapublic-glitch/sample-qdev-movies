package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Ahoy! Search the treasure chest of movies by various criteria, matey!
     * 
     * @param name Movie name to search for (partial matching, case-insensitive)
     * @param id Movie ID to search for (exact matching)
     * @param genre Movie genre to search for (partial matching, case-insensitive)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Starting treasure hunt for movies with criteria - name: '{}', id: '{}', genre: '{}'", 
                   name, id, genre);
        
        List<Movie> results = movies.stream()
            .filter(movie -> matchesSearchCriteria(movie, name, id, genre))
            .collect(Collectors.toList());
        
        logger.info("Arrr! Found {} treasures matching yer search criteria, matey!", results.size());
        return results;
    }

    /**
     * Check if a movie matches the search criteria like a true pirate examines treasure!
     */
    private boolean matchesSearchCriteria(Movie movie, String name, Long id, String genre) {
        // If searching by ID, it must match exactly
        if (id != null && !movie.getId().equals(id)) {
            return false;
        }
        
        // If searching by name, do case-insensitive partial matching
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            String movieName = movie.getMovieName().toLowerCase();
            if (!movieName.contains(searchName)) {
                return false;
            }
        }
        
        // If searching by genre, do case-insensitive partial matching
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            String movieGenre = movie.getGenre().toLowerCase();
            if (!movieGenre.contains(searchGenre)) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Validate search parameters to prevent scurvy bugs, arrr!
     */
    public boolean isValidSearchRequest(String name, Long id, String genre) {
        // At least one search parameter must be provided
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasId = id != null && id > 0;
        boolean hasGenre = genre != null && !genre.trim().isEmpty();
        
        if (!hasName && !hasId && !hasGenre) {
            logger.warn("Arrr! No search criteria provided, ye scallywag!");
            return false;
        }
        
        // Validate ID if provided
        if (id != null && id <= 0) {
            logger.warn("Arrr! Invalid movie ID provided: {}", id);
            return false;
        }
        
        return true;
    }
}
