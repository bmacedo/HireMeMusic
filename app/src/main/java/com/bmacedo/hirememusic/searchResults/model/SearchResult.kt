package com.bmacedo.hirememusic.searchResults.model

import com.squareup.moshi.Json

data class SearchResult(
    @Json(name = "artists") val artists: ArtistsPage
)