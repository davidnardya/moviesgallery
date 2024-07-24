# Movie App Documentation

## Overview

The Movie App is an Android application developed using Kotlin and Jetpack Compose. It integrates with a movie database API to display movie details, configurations, and the author's favorite movies. This documentation provides an overview of the project's structure, setup, and testing procedures.

## Project Structure

### Directories and Files

- `app/`: Contains the Android application code.
    - `src/main/java/com/davidnardya/moviesgallery/`: Contains the application code, including models, repository, view models, and activities.
    - `src/main/res/`: Contains resource files like layouts, strings, and drawable assets.
    - `src/main/AndroidManifest.xml`: The AndroidManifest file for application configuration.

- `build.gradle`: The Gradle build script for managing dependencies and build configurations.

- `README.md`: This documentation file.

## Setup and Installation

### Prerequisites

- Android Studio
- Java Development Kit (JDK) 11 or later

## Building the Project
- Open the project in Android Studio.
- Sync the project with Gradle files by clicking "Sync Now" in the banner that appears.
- Build and run the application by selecting "Run" from the menu or pressing Shift + F10.

## Dependencies
- `Jetpack Compose`: For building UI components.
- `Glide`: For image loading and caching.
- `Retrofit`: For network operations.
- `Room`: For local database operations.
- `JUnit`: For unit testing.
- `Mockito`: For mocking dependencies in tests.
- `MockWebServer`: For mocking network responses in tests.

### ViewModel

## MainViewModel
- `MainViewModel` manages UI-related data and interacts with the MainRepository.

## Key Methods
- `getConfiguration()`: Fetches configuration data from the repository.
- `getMovieDetails(movieId: String)`: Retrieves movie details for a specific movie ID.
- `getMyFavs()`: Retrieves a list of favorite movies.

### Repository

## MainRepository
- `MainRepository` handles data operations and provides data to MainViewModel.

## Key Methods
- `getConfiguration()`: Fetches configuration data from the API.
- `getMovieDetails(movieId: String)`: Retrieves movie details from the API.
- `searchMovies(query: String)`: Searches for movies based on the query.
- `getFavoriteMovies()`: Retrieves favorite movies from the local database.

### API

## MoviesApi
- MoviesApi is the Retrofit service interface for interacting with the movie database API.

## Key Endpoints
- `getConfiguration(apiKey: String)`: Retrieves configuration details.
- `getMovieDetails(movieId: String, apiKey: String)`: Retrieves details for a specific movie.
- `searchMovies(query: String, apiKey: String)`: Searches for movies based on a query.

### Database

## MoviesDao
- `MoviesDao` provides data access operations for the local database.

# Key Methods
- `insertImage(cachedImage: CachedImage)`: Inserts a cached image into the database.
- `getImage(url: String)`: Retrieves a cached image by URL.
- `deleteExpiredImages(currentTime: Long)`: Deletes images that are expired based on the provided time.

## Models
- `Configuration`: Represents configuration data for image sizes and change keys.
- `MovieDetailsResponse`: Represents the details of a movie.
- `MovieSearchResponse`: Represents search results including a list of movies.
- `CachedImage`: Represents an image cached locally.

### Testing

## Unit Tests
Unit tests cover the ViewModel, Repository, and DAO layers using JUnit, Mockito, and coroutines testing libraries.

## Example Tests
- `MainRepositoryTest`: Tests the MainRepository methods to ensure they correctly interact with the API and local database.
- `MoviesDaoTest`: Tests the MoviesDao methods to ensure they correctly handle database operations.

### Contact
For any questions or support, please contact davidnardya@gmail.com