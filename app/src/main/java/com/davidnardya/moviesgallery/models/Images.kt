package com.davidnardya.moviesgallery.models


import com.google.gson.annotations.SerializedName

data class Images(
    @SerializedName("backdrop_sizes")
    val backdropSizes: List<String>? = emptyList(),
    @SerializedName("base_url")
    val baseUrl: String? = "",
    @SerializedName("logo_sizes")
    val logoSizes: List<String>? = emptyList(),
    @SerializedName("poster_sizes")
    val posterSizes: List<String>? = emptyList(),
    @SerializedName("profile_sizes")
    val profileSizes: List<String>? = emptyList(),
    @SerializedName("secure_base_url")
    val secureBaseUrl: String? = "",
    @SerializedName("still_sizes")
    val stillSizes: List<String>? = emptyList()
)