package com.davidnardya.moviesgallery.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.davidnardya.moviesgallery.ui.screens.MainScreen
import com.davidnardya.moviesgallery.ui.screens.Screen
import com.davidnardya.moviesgallery.ui.screens.SingleMovieScreen
import com.davidnardya.moviesgallery.viewmodels.MainViewModel

@Composable
fun SetupNavGraph(
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Main.route
    ) {
        composable(
            route = Screen.Main.route
        ) {
            MainScreen(viewModel, navHostController)
        }
        composable(
            route = Screen.SingleMovie.route
        ) {
            SingleMovieScreen(viewModel)
        }
    }
}