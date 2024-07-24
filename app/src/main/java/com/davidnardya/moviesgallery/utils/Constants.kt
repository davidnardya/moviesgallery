package com.davidnardya.moviesgallery.utils
import androidx.compose.foundation.layout.PaddingValues
import com.davidnardya.moviesgallery.BuildConfig


object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val ACCESS_TOKEN = BuildConfig.ACCESS_TOKEN
    const val IMAGE_SIZE = "w780"
    var innerPadding: PaddingValues? = null

    val myFavs = listOf(
        "120",
        "121",
        "122",
        "652",
        "98",
    )
}