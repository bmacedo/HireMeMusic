package com.bmacedo.hirememusic.searchResults

import com.bmacedo.hirememusic.api.SpotifyApi
import com.bmacedo.hirememusic.searchResults.model.SearchResult

class SearchResultsRepository(private val api: SpotifyApi) {

    /**
     * Search for artists by name
     */
    suspend fun searchArtists(query: String, pageNumber: Int = 1, pageSize: Int = 20): SearchResult {
        val queryType = "artist"
        val offset = (pageNumber - 1) * pageSize
        return api.search(query, queryType, offset, pageSize).await()
    }
}