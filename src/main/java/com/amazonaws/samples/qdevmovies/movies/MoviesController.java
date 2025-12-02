package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        return "movies";
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }

    /**
     * Ahoy matey! Search for movies like a true pirate hunting for treasure!
     * 
     * @param name Movie name to search for (optional)
     * @param id Movie ID to search for (optional)
     * @param genre Movie genre to search for (optional)
     * @param model Spring model for template rendering
     * @return Template name for rendering search results
     */
    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Captain's orders to search for movies - name: '{}', id: '{}', genre: '{}'", 
                   name, id, genre);
        
        // Validate search parameters like a careful pirate checking the treasure map
        if (!movieService.isValidSearchRequest(name, id, genre)) {
            logger.warn("Arrr! Invalid search request, ye scallywag!");
            model.addAttribute("title", "Search Error");
            model.addAttribute("message", "Arrr! Ye need to provide at least one search criterion, matey! " +
                             "Try searching by movie name, ID, or genre.");
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            return "error";
        }
        
        try {
            // Search the treasure chest of movies
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Prepare the treasure for display
            model.addAttribute("movies", searchResults);
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            model.addAttribute("searchResultsCount", searchResults.size());
            
            if (searchResults.isEmpty()) {
                logger.info("Arrr! No treasure found matching the search criteria, matey!");
                model.addAttribute("noResultsMessage", 
                    "Arrr! No movies found matching yer search criteria, matey! " +
                    "Try adjusting yer search terms and sail again!");
            } else {
                logger.info("Shiver me timbers! Found {} movies matching the search, ye savvy sailor!", 
                           searchResults.size());
            }
            
            return "movies";
            
        } catch (Exception e) {
            logger.error("Blimey! Error occurred during movie search: {}", e.getMessage(), e);
            model.addAttribute("title", "Search Error");
            model.addAttribute("message", "Arrr! Something went wrong during the search, matey! " +
                             "The crew is working to fix this issue. Please try again later.");
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            return "error";
        }
    }
}