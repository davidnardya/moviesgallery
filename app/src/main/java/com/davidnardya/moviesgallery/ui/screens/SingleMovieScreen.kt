package com.davidnardya.moviesgallery.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.davidnardya.moviesgallery.R
import com.davidnardya.moviesgallery.utils.Constants
import com.davidnardya.moviesgallery.viewmodels.MainViewModel
import kotlin.math.roundToInt

@Composable
fun SingleMovieScreen(viewModel: MainViewModel) {

    val movieState by viewModel.movieDetailsLiveData.observeAsState()
    val configState by viewModel.configLiveData.observeAsState()
    val trailerUrl by viewModel.trailerUrl.observeAsState()
    val showTrailerPlayer = remember { mutableStateOf(false) }

    val imageBaseUrl = configState?.images?.secureBaseUrl
    val imageSize =
        configState?.images?.posterSizes?.first { it.lowercase() == Constants.IMAGE_SIZE }
    val src = "$imageBaseUrl$imageSize${movieState?.posterPath}"
    val context = LocalContext.current

    LaunchedEffect(movieState) {
        viewModel.fetchTrailerUrl(movieState?.id ?: -1)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(src)
                    .build(),
                contentDescription = "${movieState?.title} image",
                placeholder = painterResource(R.drawable.placeholder_image),
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .aspectRatio(2f / 3f),
                contentScale = ContentScale.Crop
            )
        }
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        trailerUrl?.let {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                        showTrailerPlayer.value = !showTrailerPlayer.value
                    },
                    enabled = trailerUrl != null
                ) {
                    Text("View Trailer")
                }

                Button(
                    onClick = {
                        val shareText = "Check out this movie: ${movieState?.title}\n${movieState?.overview}"
                        val intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(intent, "Share Movie"))
                    }
                ) {
                    Text("Share")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movieState?.title.toString(),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movieState?.tagline.toString(),
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Original Language: ${movieState?.originalLanguage.toString()}",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Release Year: ${movieState?.releaseDate?.split("-")?.first()}",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rating: ${movieState?.voteAverage?.roundToInt()}/10",
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movieState?.overview.toString(),
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}