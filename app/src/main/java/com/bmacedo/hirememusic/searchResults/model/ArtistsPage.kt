package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class ArtistsPage(
    @Json(name = "items") val items: List<Artist>
)