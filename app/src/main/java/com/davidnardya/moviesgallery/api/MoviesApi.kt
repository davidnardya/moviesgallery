package com.davidnardya.moviesgallery.api

import com.davidnardya.moviesgallery.models.Configuration
import com.davidnardya.moviesgallery.models.MovieDetailsResponse
import com.davidnardya.moviesgallery.models.MovieSearchResponse
import com.davidnardya.moviesgallery.models.MovieVideosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query


interface MoviesApi {

    @GET("configuration")
    suspend fun getConfiguration(
        @Header("Authorization") authorization: String
    ): Configuration

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Header("Authorization") authorization: String,
        @Path("movie_id") movieId: String
    ): MovieDetailsResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Header("Authorization") authorization: String,
        @Query("query") query: String,
    ): MovieSearchResponse

    @GET("movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Header("Authorization") authorization: String,
        @Path("movie_id") movieId: Int
    ): MovieVideosResponse
}