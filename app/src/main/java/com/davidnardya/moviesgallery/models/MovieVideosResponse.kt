package com.davidnardya.moviesgallery.models

data class MovieVideosResponse(
    val results: List<Video>
)

data class Video(
    val key: String,
    val name: String,
    val site: String,
    val type: String
)