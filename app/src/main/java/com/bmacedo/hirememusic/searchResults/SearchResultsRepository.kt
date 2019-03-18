package com.bmacedo.hirememusic.searchResults

import com.bmacedo.hirememusic.api.SpotifyApi
import com.bmacedo.hirememusic.searchResults.model.SearchResult

class SearchResultsRepository(private val api: SpotifyApi) {

    /**
     * Search for artists by name
     */
    suspend fun searchArtists(query: String): SearchResult {
        val queryType = "artist"
        return api.search(query, queryType).await()
    }
}