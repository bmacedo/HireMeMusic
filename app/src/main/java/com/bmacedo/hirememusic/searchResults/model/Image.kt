package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class Image(
    @Json(name = "height") val height: Int,
    @Json(name = "width") val width: Int,
    @Json(name = "url") val url: Int
)