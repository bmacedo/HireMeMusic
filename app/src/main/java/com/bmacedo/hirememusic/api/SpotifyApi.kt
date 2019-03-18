package com.bmacedo.hirememusic.api

import com.bmacedo.hirememusic.searchResults.model.SearchResult
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotifyApi {

    @GET("v1/search")
    fun search(@Query("q") query: String, @Query("type") queryType: String): Deferred<SearchResult>
}