package com.bmacedo.hirememusic.searchResults

import android.content.res.Resources
import androidx.lifecycle.*
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import com.bmacedo.hirememusic.searchResults.model.Artist
import com.bmacedo.hirememusic.searchResults.model.SearchResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SearchResultsViewModel(
    private val searchResultsRepository: SearchResultsRepository,
    private val authenticationRepository: AuthenticationRepository,
    private val resources: Resources
) : ViewModel() {

    /**
     * Returns the index of the list item last visible
     */
    var lastVisibleItem: Int = 0
    /**
     * Returns the number of items already loaded
     */
    var totalItemCount: Int = 0

    private var pageNumber = 1
    private var total = 0
    private val viewState: MutableLiveData<ViewState> = MutableLiveData()
    private var latestQuery: String = ""
    private var job: Job? = null

    /**
     * Starts the search for a list of [Artist] with a given query
     */
    fun queryArtist(query: String, page: Int = 1) {
        if (job != null) {
            job?.cancel()
        }
        if (query.isNotEmpty()) {
            viewState.postValue(ViewState.Success(isLoading = true, artists = latestResults()))
            job = viewModelScope.launch {
                latestQuery = query
                try {
                    val results =
                        searchResultsRepository.searchArtists(query = query, pageNumber = page, pageSize = PAGE_SIZE)
                    handleSuccess(results)
                } catch (cancellationException: CancellationException) {
                    // nothing to do here. Suppress the exception since this is the normal flow
                } catch (httpException: HttpException) {
                    handleNetworkError(httpException)
                } catch (exception: Exception) {
                    handleGenericError(exception)
                }
            }
        } else {
            viewState.postValue(ViewState.Success(isLoading = false, artists = latestResults()))
        }
    }

    /**
     * Returns more artists for the same query. Allows for pagination.
     */
    fun queryForMore() {
        if (totalItemCount < total) {
            pageNumber++
            queryArtist(latestQuery, pageNumber)
        }
    }

    /**
     * Queries the same artist again, starting from the first page
     */
    fun resetSearch() {
        pageNumber = 1
        queryArtist(latestQuery)
    }

    /**
     * Returns true if should load next page
     */
    fun shouldLoadNextPage(): Boolean {
        val state = viewState.value
        val isLoading = (state is ViewState.Success) && state.isLoading
        return !isLoading && totalItemCount < total && totalItemCount <= lastVisibleItem + VISIBLE_THRESHOLD
    }

    /**
     * Returns the current view state
     */
    fun viewState(): LiveData<ViewState> = viewState

    private fun handleGenericError(exception: Exception) {
        val errorMessage = exception.message ?: resources.getString(R.string.generic_error)
        viewState.postValue(ViewState.Error(errorMessage))
    }

    private fun handleNetworkError(httpException: HttpException) {
        if (httpException.code() == 401) {
            handleAuthenticationError()
        } else {
            val errorMessage = resources.getString(R.string.generic_network_error)
            viewState.postValue(ViewState.Error(errorMessage))
        }
    }

    private fun handleAuthenticationError() {
        viewModelScope.launch {
            authenticationRepository.invalidateToken()
            viewState.postValue(ViewState.AuthenticationError)
        }
    }

    private fun handleSuccess(results: SearchResult) {
        this.total = results.artists.total
        val artists = appendWithPreviousPages(results.artists.items)
        viewState.postValue(ViewState.Success(isLoading = false, artists = artists))
    }

    private fun latestResults(): List<Artist> {
        val state = viewState.value
        if (state is ViewState.Success) {
            return state.artists
        }
        return emptyList()
    }

    private fun appendWithPreviousPages(articles: List<Artist>): List<Artist> {
        val state = viewState.value
        val previousArtists = if (state is ViewState.Success && pageNumber > 1) {
            state.artists
        } else {
            mutableListOf()
        }
        return previousArtists + articles
    }

    /**
     * Possible states for this View
     */
    sealed class ViewState {
        // Success may include loading as well for pagination
        data class Success(
            val isLoading: Boolean = false,
            val artists: List<Artist> = emptyList()
        ) : ViewState()

        // Error thrown when the user token is no longer valid
        object AuthenticationError : ViewState()

        // Generic error
        data class Error(val message: String) : ViewState()
    }

    /**
     * Factory class for the ViewModel
     */
    class Factory(
        private val searchResultsRepository: SearchResultsRepository,
        private val authenticationRepository: AuthenticationRepository,
        private val resources: Resources
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
                return SearchResultsViewModel(searchResultsRepository, authenticationRepository, resources) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
        private const val VISIBLE_THRESHOLD = 1
    }
}