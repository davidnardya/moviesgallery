package com.davidnardya.moviesgallery.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.davidnardya.moviesgallery.R
import com.davidnardya.moviesgallery.models.MovieDetailsResponse
import com.davidnardya.moviesgallery.utils.Constants.IMAGE_SIZE
import com.davidnardya.moviesgallery.viewmodels.MainViewModel

@Composable
fun MainScreen(viewModel: MainViewModel, navController: NavHostController) {

    val configState by viewModel.configLiveData.observeAsState()
    val favoritesState by viewModel.favoriteMovies.observeAsState()
    val searchResult by viewModel.searchResults.observeAsState()
    val imageBitmaps by viewModel.imageBitmaps.observeAsState(emptyMap())

    var searchQuery by remember { mutableStateOf("") }
    var showLoader by remember { mutableStateOf(false) }

    val onSearchQueryChange: (String) -> Unit = { query ->
        searchQuery = query
    }

    val onSearchClick: () -> Unit = {
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMovies(searchQuery)
            showLoader = true
        }
    }

    val context = LocalContext.current
    val imageBaseUrl = configState?.images?.secureBaseUrl
    val imageSize = configState?.images?.posterSizes?.first { it.lowercase() == IMAGE_SIZE }

    LaunchedEffect(favoritesState) {
        favoritesState?.map { "$imageBaseUrl$imageSize${it.posterPath}" }?.let {
            viewModel.getImages(
                urls = it,
                context = context
            )
        }
    }

    LaunchedEffect(searchResult) {
        searchResult?.map { "$imageBaseUrl$imageSize${it.posterPath}" }?.let {
            viewModel.getImages(
                urls = it,
                context = context
            )
        }
        showLoader = searchResult?.isEmpty() == true
    }

    Column {
        AuthorsSuggestions(favoritesState, imageBaseUrl, imageSize, imageBitmaps) { id ->
            viewModel.getMovieDetails(id)
            navController.navigate(Screen.SingleMovie.route)
        }

        SearchBar(searchQuery, onSearchQueryChange, onSearchClick)


        if(showLoader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
            }
        } else {
            searchResult?.let { movieList ->
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 120.dp),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(movieList) { movie ->
                        val src = "$imageBaseUrl$imageSize${movie.posterPath}"
                        val bitmap = imageBitmaps[src]

                        MovieBox(movie, bitmap) { id ->
                            viewModel.getMovieDetails(id)
                            navController.navigate(Screen.SingleMovie.route)
                        }
                    }
                }
            }
        }

    }


}

@Composable
private fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            TextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = { Text("Search...") },
                singleLine = true
            )
            Button(
                onClick = onSearchClick,
                enabled = searchQuery.isNotEmpty()
            ) {
                Text("Search")
            }
        }
    }
}

@Composable
private fun AuthorsSuggestions(
    favoritesState: List<MovieDetailsResponse>?,
    imageBaseUrl: String?,
    imageSize: String?,
    imageBitmaps: Map<String, Bitmap?>,
    clickable: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Author's suggestions:", style = MaterialTheme.typography.titleLarge)
        LazyRow {
            favoritesState?.let { favList ->
                items(favList) { fav ->
                    val src = "$imageBaseUrl$imageSize${fav.posterPath}"
                    val bitmap = imageBitmaps[src]

                    MovieBox(movie = fav, bitmap = bitmap, clickable = clickable)
                }
            }
        }
    }
}

@Composable
fun MovieBox(movie: MovieDetailsResponse, bitmap: Bitmap?, clickable: (String) -> Unit) {

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val boxSize = screenWidth / 3

    Column(
        modifier = Modifier.clickable { clickable(movie.id.toString()) }
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "${movie.title} image",
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .width(boxSize)
                    .aspectRatio(2f / 3f),
            )
        } else {
            Image(
                painter = painterResource(R.drawable.placeholder_image),
                contentDescription = "${movie.title} image",
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .width(boxSize)
                    .aspectRatio(2f / 3f),
            )
        }

        Text(
            text = movie.title.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 4.dp)
                .width(boxSize)
        )
    }

}