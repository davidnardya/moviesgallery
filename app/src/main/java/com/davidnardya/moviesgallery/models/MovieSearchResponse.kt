package com.davidnardya.moviesgallery.models

import com.google.gson.annotations.SerializedName

data class MovieSearchResponse(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val results: List<MovieDetailsResponse>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)