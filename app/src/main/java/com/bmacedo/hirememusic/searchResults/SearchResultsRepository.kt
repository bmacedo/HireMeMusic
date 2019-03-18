package com.bmacedo.hirememusic.searchResults

import com.bmacedo.hirememusic.api.SpotifyApi

class SearchResultsRepository(private val api: SpotifyApi) {

    /**
     * Search for artists by name
     */
    fun searchArtists(query: String) {
        // TODO
        val queryType = "artist"
        api.search(query, queryType)
    }
}