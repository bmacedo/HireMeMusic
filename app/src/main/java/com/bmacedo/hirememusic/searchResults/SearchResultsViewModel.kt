package com.bmacedo.hirememusic.searchResults

import android.content.res.Resources
import androidx.lifecycle.*
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.searchResults.model.Artist
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchResultsViewModel(
    private val searchResultsRepository: SearchResultsRepository,
    private val resources: Resources
) : ViewModel() {

    private val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private var latestQuery: String = ""
    private var job: Job? = null

    /**
     * Starts the search for a list of [Artist] with a given query
     */
    fun queryArtist(query: String) {
        if (job != null) {
            job?.cancel()
        }
        viewState.postValue(ViewState.Success(isLoading = true, artists = emptyList()))
        job = viewModelScope.launch {
            latestQuery = query
            try {
                val results = searchResultsRepository.searchArtists(query)
                val artists = results.artists.items
                viewState.postValue(ViewState.Success(isLoading = false, artists = artists))
            } catch (exception: Exception) {
                val errorMessage = exception.message ?: resources.getString(R.string.generic_error)
                viewState.postValue(ViewState.Error(errorMessage))
            }
        }
    }

    /**
     * Returns more artists for the same query. Allows for pagination.
     */
    fun queryForMore() {
        // TODO
    }

    /**
     * Queries the same artist again, starting from the first page
     */
    fun resetSearch() {
        queryArtist(latestQuery)
    }

    /**
     * Returns the current view state
     */
    fun viewState(): LiveData<ViewState> = viewState

    /**
     * Possible states for this View
     */
    sealed class ViewState {

        data class Success(
            val isLoading: Boolean = false,
            val artists: List<Artist> = emptyList()
        ) : ViewState()

        data class Error(val message: String) : ViewState()
    }

    /**
     * Factory class for the ViewModel
     */
    class Factory(
        private val searchResultsRepository: SearchResultsRepository,
        private val resources: Resources
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
                return SearchResultsViewModel(searchResultsRepository, resources) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}