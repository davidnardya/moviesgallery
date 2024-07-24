package com.davidnardya.moviesgallery.repositories

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.bumptech.glide.Glide
import com.davidnardya.moviesgallery.api.MoviesApi
import com.davidnardya.moviesgallery.dao.MoviesDao
import com.davidnardya.moviesgallery.models.CachedImage
import com.davidnardya.moviesgallery.models.Configuration
import com.davidnardya.moviesgallery.models.MovieDetailsResponse
import com.davidnardya.moviesgallery.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val moviesApi: MoviesApi,
    private val moviesDao: MoviesDao,
) {
    private val TAG = this::class.java.simpleName

    suspend fun getConfiguration(): Configuration? = try {
        moviesApi.getConfiguration(Constants.ACCESS_TOKEN)
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        null
    }

    suspend fun getMovieDetails(movieId: String): MovieDetailsResponse? = try {
        moviesApi.getMovieDetails(
            authorization = Constants.ACCESS_TOKEN,
            movieId = movieId
        )
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        null
    }

    suspend fun searchMovies(query: String): List<MovieDetailsResponse>? = try {
        val response = moviesApi.searchMovies(
            authorization = Constants.ACCESS_TOKEN,
            query = query
        )
        response.results
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        null
    }

    suspend fun getMovieTrailerUrl(movieId: Int): String? = try {
        val response = moviesApi.getMovieVideos(
            authorization = Constants.ACCESS_TOKEN,
            movieId = movieId
        )
        response.results.firstOrNull { it.type == "Trailer" }?.let {
            "https://www.youtube.com/watch?v=${it.key}"
        }
    } catch (e: Exception) {
        Log.e(TAG, e.message.toString())
        null
    }

    suspend fun getCachedImage(url: String): Bitmap? {
        val cachedImage = moviesDao.getImage(url)
        val expirationTime = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1)
        if (cachedImage != null && cachedImage.cachedAt > expirationTime) {
            return BitmapFactory.decodeFile(cachedImage.filePath)
        } else {
            cachedImage?.let {
                File(it.filePath).delete()
                moviesDao.deleteExpiredImages(expirationTime)
            }
            return null
        }
    }


    suspend fun cacheImage(url: String, context: Context): Bitmap? {
        val cacheDir = context.cacheDir
        val bitmap = withContext(Dispatchers.IO) { downloadImageWithGlide(context, url) }
        if (bitmap != null) {
            val filePath = saveImageToFile(bitmap, url, cacheDir)
            moviesDao.insertImage(
                CachedImage(
                    url = url,
                    filePath = filePath,
                    cachedAt = System.currentTimeMillis()
                )
            )
        }
        return bitmap
    }

    private fun saveImageToFile(bitmap: Bitmap, url: String, cacheDir: File): String {
        val fileName = url.hashCode().toString()
        val file = File(cacheDir, fileName)
        FileOutputStream(file).use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
        }
        return file.absolutePath
    }

    private fun downloadImageWithGlide(context: Context, url: String): Bitmap? {
        return try {
            Glide.with(context)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            Log.e(TAG, e.message.toString())
            null
        }
    }
}