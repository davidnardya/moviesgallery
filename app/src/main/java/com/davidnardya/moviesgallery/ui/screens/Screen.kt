package com.davidnardya.moviesgallery.ui.screens

sealed class Screen(val route: String) {
    data object Main : Screen("MainScreen")
    data object SingleMovie : Screen("SingleMovieScreen")
}