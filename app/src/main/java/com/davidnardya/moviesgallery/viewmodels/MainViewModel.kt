package com.davidnardya.moviesgallery.viewmodels

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.davidnardya.moviesgallery.models.Configuration
import com.davidnardya.moviesgallery.models.MovieDetailsResponse
import com.davidnardya.moviesgallery.repositories.MainRepository
import com.davidnardya.moviesgallery.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    init {
        getConfiguration()
        getMyFavs()
    }

    val configLiveData = MutableLiveData<Configuration?>()
    val movieDetailsLiveData = MutableLiveData<MovieDetailsResponse?>()
    val favoriteMovies = MutableLiveData<List<MovieDetailsResponse>>()
    val searchResults = MutableLiveData<List<MovieDetailsResponse>?>()
    val trailerUrl = MutableLiveData<String?>()
    val imageBitmaps = MutableLiveData<Map<String, Bitmap?>>()

    fun getConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async { mainRepository.getConfiguration() }.await()
            configLiveData.postValue(response)
        }
    }

    fun getMovieDetails(movieId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async { mainRepository.getMovieDetails(movieId) }.await()
            movieDetailsLiveData.postValue(response)
        }
    }

    fun getMyFavs() {
        viewModelScope.launch(Dispatchers.IO) {
            val newList = emptyList<MovieDetailsResponse>().toMutableList()
            Constants.myFavs.forEach {
                val response = async { mainRepository.getMovieDetails(it) }.await()
                if (response != null) {
                    newList.add(response)
                }
            }
            favoriteMovies.postValue(newList)
        }
    }

    fun searchMovies(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = async { mainRepository.searchMovies(query) }.await()
            searchResults.postValue(response)
        }
    }

    fun fetchTrailerUrl(movieId: Int) {
        viewModelScope.launch {
            val response = async { mainRepository.getMovieTrailerUrl(movieId) }.await()
            trailerUrl.postValue(response)
        }
    }

    fun getImages(urls: List<String>, context: Context) {
        viewModelScope.launch {
            val newBitmaps = imageBitmaps.value?.toMutableMap() ?: mutableMapOf()
            for (url in urls) {
                val cachedImage = mainRepository.getCachedImage(url)
                if (cachedImage != null) {
                    newBitmaps[url] = cachedImage
                } else {
                    val downloadedImage = mainRepository.cacheImage(url, context)
                    newBitmaps[url] = downloadedImage
                }
            }
            imageBitmaps.postValue(newBitmaps)
        }
    }

}