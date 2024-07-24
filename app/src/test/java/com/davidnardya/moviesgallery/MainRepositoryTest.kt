package com.davidnardya.moviesgallery

import com.davidnardya.moviesgallery.api.MoviesApi
import com.davidnardya.moviesgallery.dao.MoviesDao
import com.davidnardya.moviesgallery.models.Configuration
import com.davidnardya.moviesgallery.models.MovieDetailsResponse
import com.davidnardya.moviesgallery.models.MovieSearchResponse
import com.davidnardya.moviesgallery.repositories.MainRepository
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.*
import org.mockito.*
import org.mockito.junit.*

@RunWith(MockitoJUnitRunner::class)
class MainRepositoryTest {

    @Mock
    private lateinit var moviesApi: MoviesApi

    @Mock
    private lateinit var moviesDao: MoviesDao

    private lateinit var mainRepository: MainRepository

    @Before
    fun setUp() {
        mainRepository = MainRepository(moviesApi, moviesDao)
    }

    @Test
    fun `getConfiguration returns configuration`() = runBlocking {
        val mockConfiguration = Configuration()
        Mockito.`when`(moviesApi.getConfiguration(Mockito.anyString())).thenReturn(mockConfiguration)

        val result = mainRepository.getConfiguration()

        Assert.assertEquals(mockConfiguration, result)
    }

    @Test
    fun `getMovieDetails returns movie details`() = runBlocking {
        val mockMovieDetails = MovieDetailsResponse()
        Mockito.`when`(moviesApi.getMovieDetails(Mockito.anyString(), Mockito.anyString())).thenReturn(mockMovieDetails)

        val result = mainRepository.getMovieDetails("123")

        Assert.assertEquals(mockMovieDetails, result)
    }

    @Test
    fun `searchMovies returns list of movies`() = runBlocking {
        val mockMovieSearchResponse = MovieSearchResponse(
            page = 1,
            results = listOf(MovieDetailsResponse(), MovieDetailsResponse()),
            totalResults = 2,
            totalPages = 1
        )
        Mockito.`when`(moviesApi.searchMovies(Mockito.anyString(), Mockito.anyString())).thenReturn(mockMovieSearchResponse)

        val result = mainRepository.searchMovies("query")

        Assert.assertEquals(mockMovieSearchResponse.results, result)
    }
}
