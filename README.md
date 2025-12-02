# Movie Service - Spring Boot Demo Application 🏴‍☠️

Ahoy matey! A treasure chest of movies built with Spring Boot, demonstrating Java application development best practices with a swashbuckling twist!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information like a true pirate examining treasure
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🔍 Movie Search & Filtering**: Search the seven seas of movies by name, ID, or genre with our new pirate-powered search functionality!
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices, from ship to shore
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **Pirate-Themed Interface**: Arrr! Experience movie browsing with authentic pirate language and emojis

## 🆕 New Search Features

### Search the Treasure Chest of Movies!
Our new search functionality allows ye to find movies like a skilled treasure hunter:

- **Search by Name**: Find movies with partial name matching (case-insensitive)
- **Search by ID**: Locate specific movies by their unique identifier
- **Search by Genre**: Filter movies by genre (Drama, Action, Sci-Fi, etc.)
- **Combined Search**: Use multiple criteria simultaneously for precise treasure hunting
- **Smart Validation**: Prevents invalid searches and provides helpful error messages
- **Empty Results Handling**: Friendly pirate messages when no treasure is found

### Search Examples
```
# Search by movie name
http://localhost:8080/movies/search?name=Prison

# Search by movie ID
http://localhost:8080/movies/search?id=1

# Search by genre
http://localhost:8080/movies/search?genre=Drama

# Combined search
http://localhost:8080/movies/search?name=The&genre=Drama
```

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Thymeleaf** for HTML templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List with Search**: http://localhost:8080/movies
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)
- **Movie Search**: http://localhost:8080/movies/search?name={name}&id={id}&genre={genre}

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── movies/
│   │       │   ├── MoviesApplication.java    # Main Spring Boot application
│   │       │   ├── MoviesController.java     # REST controller with search endpoint
│   │       │   ├── MovieService.java         # Service layer with search functionality
│   │       │   ├── Movie.java                # Movie data model
│   │       │   ├── Review.java               # Review data model
│   │       │   └── ReviewService.java        # Review service
│   │       └── utils/
│   │           ├── MovieIconUtils.java       # Movie icon utilities
│   │           └── MovieUtils.java           # Movie validation utilities
│   └── resources/
│       ├── templates/
│       │   ├── movies.html                   # Enhanced with search form
│       │   ├── movie-details.html            # Movie details template
│       │   └── error.html                    # Error handling template
│       ├── static/css/                       # Stylesheets
│       ├── application.yml                   # Application configuration
│       ├── movies.json                       # Movie data
│       ├── mock-reviews.json                 # Mock review data
│       └── log4j2.xml                        # Logging configuration
└── test/                                     # Comprehensive unit tests
    └── java/
        └── com/amazonaws/samples/qdevmovies/movies/
            ├── MovieServiceTest.java         # Service layer tests
            ├── MoviesControllerTest.java     # Controller tests
            └── MovieTest.java                # Model tests
```

## API Endpoints

### Get All Movies (Enhanced with Search Form)
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and a search form.

### 🆕 Search Movies
```
GET /movies/search
```
Search and filter movies based on various criteria.

**Query Parameters:**
- `name` (optional): Movie name for partial matching (case-insensitive)
- `id` (optional): Exact movie ID (must be positive integer)
- `genre` (optional): Genre for partial matching (case-insensitive)

**Examples:**
```
# Search for movies with "Prison" in the name
GET /movies/search?name=Prison

# Find movie with ID 1
GET /movies/search?id=1

# Find all Drama movies
GET /movies/search?genre=Drama

# Combined search: Drama movies with "The" in the name
GET /movies/search?name=The&genre=Drama
```

**Response:**
- Returns HTML page with search results
- Displays search criteria and result count
- Shows friendly error messages for invalid searches
- Provides "no results" message with pirate flair when no movies match

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Search Functionality Details

### Validation Rules
- At least one search parameter must be provided
- Movie ID must be a positive integer if provided
- Empty or whitespace-only strings are treated as not provided
- Case-insensitive matching for name and genre searches

### Error Handling
- Invalid search requests return user-friendly error pages
- Search parameters are preserved in error responses for easy correction
- Comprehensive logging with pirate-themed messages for debugging

### Edge Cases Handled
- Empty search results with helpful suggestions
- Invalid parameter combinations
- Null and empty parameter values
- Whitespace-only parameters
- Non-existent movie IDs

## Testing

Run the comprehensive test suite:

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieServiceTest

# Run tests with coverage
mvn test jacoco:report
```

### Test Coverage
- **MovieService**: Search functionality, validation, edge cases
- **MoviesController**: Search endpoint, error handling, parameter validation
- **Integration Tests**: End-to-end search scenarios

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Check that at least one search parameter is provided
2. Verify movie IDs are positive integers
3. Check application logs for detailed error messages
4. Ensure movies.json is properly loaded

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the catalog
- Enhance the search functionality (e.g., year-based filtering)
- Improve the UI/UX with more pirate themes
- Add new features like advanced filtering or sorting
- Enhance the responsive design for better mobile experience

### Development Guidelines
- Follow existing pirate-themed naming conventions in comments and logs
- Maintain comprehensive test coverage for new features
- Use proper input validation and error handling
- Follow Spring Boot best practices

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

*Arrr! May yer code be bug-free and yer deployments smooth sailing! 🚢⚓*
