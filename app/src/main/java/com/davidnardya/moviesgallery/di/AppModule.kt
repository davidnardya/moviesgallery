package com.davidnardya.moviesgallery.di

import android.content.Context
import androidx.room.Room
import com.davidnardya.moviesgallery.R
import com.davidnardya.moviesgallery.api.MoviesApi
import com.davidnardya.moviesgallery.api.createRetrofitInstance
import com.davidnardya.moviesgallery.dao.MoviesDao
import com.davidnardya.moviesgallery.db.MoviesDatabase
import com.davidnardya.moviesgallery.repositories.MainRepository
import com.davidnardya.moviesgallery.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        moviesApi: MoviesApi,
        moviesDao: MoviesDao,
    ) = MainRepository(moviesApi, moviesDao)

    @Singleton
    @Provides
    fun provideMoviesApi(): MoviesApi = createRetrofitInstance(BASE_URL)

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        @ApplicationContext context: Context,
    ): MoviesDatabase = Room.databaseBuilder(
        context,
        MoviesDatabase::class.java,
        context.getString(R.string.cached_images),
    ).build()

    @Singleton
    @Provides
    fun provideMoviesDao(dataBase: MoviesDatabase) = dataBase.moviesDao()
}