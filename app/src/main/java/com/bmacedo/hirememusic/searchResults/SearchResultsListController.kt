package com.bmacedo.hirememusic.searchResults

import android.content.Intent
import android.net.Uri
import com.airbnb.epoxy.EpoxyController
import com.bmacedo.hirememusic.searchResults.model.Artist
import com.bmacedo.hirememusic.searchResultsArtistItem

class SearchResultsListController : EpoxyController() {

    private val artistsList = mutableListOf<Artist>()

    fun update(artists: List<Artist>) {
        artistsList.clear()
        artistsList.addAll(artists)
        requestModelBuild()
    }

    override fun buildModels() {
        artistsList.forEach { artist ->
            searchResultsArtistItem {
                id(artist.id)
                artist(artist)
                onArtistClicked { view ->
                    if (artist.artistUrl.isNotBlank()) {
                        val context = view.context
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(artist.artistUrl)
                        context?.startActivity(intent)
                    }
                }
            }
        }
    }
}