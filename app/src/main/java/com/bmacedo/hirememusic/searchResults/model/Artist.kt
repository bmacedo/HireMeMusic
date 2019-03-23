package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class Artist(
    @Json(name = "external_urls") val urlMap: Map<String, String> = emptyMap(),
    @Json(name = "genres") val genres: List<String> = emptyList(),
    @Json(name = "href") val artistUrl: String = "",
    @Json(name = "id") val id: String = "",
    @Json(name = "images") val images: List<Image> = emptyList(),
    @Json(name = "name") val name: String = ""
) {

    /**
     * Returns the URL for the smallest image available for the artist
     */
    fun thumbnailUrl(): String? {
        if (images.isNotEmpty()) {
            val thumbnail = images.firstOrNull { it.height == 300 } ?: images.firstOrNull()
            return thumbnail?.url
        }
        return null
    }

    /**
     * Returns the external URL thar contains more information about the artist
     */
    fun externalUrl(): String? {
        if (urlMap.isNotEmpty()) {
            return urlMap.values.firstOrNull()
        }
        return null
    }

}