package com.davidnardya.moviesgallery.models


import com.google.gson.annotations.SerializedName

data class Configuration(
    @SerializedName("change_keys")
    val changeKeys: List<String>? = emptyList(),
    @SerializedName("images")
    val images: Images? = null
)