package com.davidnardya.moviesgallery.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.davidnardya.moviesgallery.dao.MoviesDao
import com.davidnardya.moviesgallery.models.CachedImage

@Database(entities = [CachedImage::class], version = 1, exportSchema = false)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao
}