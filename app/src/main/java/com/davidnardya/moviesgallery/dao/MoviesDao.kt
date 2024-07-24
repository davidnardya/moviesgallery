package com.davidnardya.moviesgallery.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.davidnardya.moviesgallery.models.CachedImage

@Dao
interface MoviesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImage(cachedImage: CachedImage)

    @Query("SELECT * FROM cached_images WHERE url = :url")
    suspend fun getImage(url: String): CachedImage?

    @Query("DELETE FROM cached_images WHERE cachedAt < :expirationTime")
    suspend fun deleteExpiredImages(expirationTime: Long)
}