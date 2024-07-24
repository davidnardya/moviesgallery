package com.davidnardya.moviesgallery.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_images")
data class CachedImage(
    @PrimaryKey val url: String,
    val filePath: String,
    val cachedAt: Long
)