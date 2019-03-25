package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class ArtistsPage(
    @Json(name = "items") val items: List<Artist>,
    @Json(name = "next") val nextUrl: String?,
    @Json(name = "limit") val pageSize: Int,
    @Json(name = "offset") val offset: Int,
    @Json(name = "total") val total: Int
)