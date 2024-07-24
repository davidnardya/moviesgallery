package com.davidnardya.moviesgallery

import com.davidnardya.moviesgallery.api.MoviesApi
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class MoviesApiTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var moviesApi: MoviesApi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        val logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        moviesApi = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MoviesApi::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getConfiguration returns configuration`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{ \"images\": {}, \"change_keys\": [] }")
        mockWebServer.enqueue(mockResponse)

        val result = moviesApi.getConfiguration("Bearer test_token")

        Assert.assertNotNull(result)
        Assert.assertEquals(0, result.changeKeys?.size)
    }

    @Test
    fun `getMovieDetails returns movie details`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("{ \"id\": 123, \"title\": \"Test Movie\" }")
        mockWebServer.enqueue(mockResponse)

        val result = moviesApi.getMovieDetails("123", "Bearer test_token")

        Assert.assertNotNull(result)
        Assert.assertEquals(123, result.id)
        Assert.assertEquals("Test Movie", result.title)
    }
}

