package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class Artist(
    @Json(name = "external_urls") val urlMap: Map<String, String> = emptyMap(),
    @Json(name = "genres") val genres: List<String> = emptyList(),
    @Json(name = "href") val artistUrl: String = "",
    @Json(name = "id") val id: String = "",
    @Json(name = "images") val images: List<Image> = emptyList(),
    @Json(name = "name") val name: List<Image> = emptyList()
)