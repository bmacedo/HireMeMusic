package com.bmacedo.hirememusic.searchResults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment
import com.bmacedo.hirememusic.searchResults.model.Artist
import com.bmacedo.hirememusic.util.EditTextDebounce
import com.bmacedo.hirememusic.util.observe
import kotlinx.android.synthetic.main.fragment_search_results.*
import javax.inject.Inject


class SearchResultsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: SearchResultsViewModel.Factory

    private val viewModel: SearchResultsViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchResultsViewModel::class.java)
    }

    private val listController = SearchResultsListController()
    private var debounce: EditTextDebounce? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = ANIMATION_DURATION_IN_MILLIS
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = ANIMATION_DURATION_IN_MILLIS
        }
        return inflater.inflate(com.bmacedo.hirememusic.R.layout.fragment_search_results, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchResultList.layoutManager = LinearLayoutManager(context)
        searchResultList.adapter = listController.adapter
        searchResultsSwipeToRefresh.setOnRefreshListener {
            viewModel.resetSearch()
        }
        debounce = EditTextDebounce.create(searchField)
        debounce?.watch(object : EditTextDebounce.DebounceCallback {
            override fun onFinished(result: String) {
                viewModel.queryArtist(result)
            }
        }, 500L)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        debounce?.unwatch()
    }

    private fun observeViewState() {
        viewModel.viewState().observe(this) { viewState ->
            refreshUi(viewState)
        }
    }

    private fun refreshUi(viewState: SearchResultsViewModel.ViewState?) {
        when (viewState) {
            is SearchResultsViewModel.ViewState.Success -> handleSuccess(viewState.isLoading, viewState.artists)
            is SearchResultsViewModel.ViewState.AuthenticationError -> handleAuthenticationError()
            is SearchResultsViewModel.ViewState.Error -> handleError(viewState.message)
            null -> throw IllegalStateException("SearchResultsViewState should not be null")
        }
    }

    private fun handleAuthenticationError() {
        context?.let { ctx ->
            // TODO: finish the dialog creation
            val dialog = AlertDialog.Builder(ctx)
                .setMessage(R.string.authentication_error_message)
                .create()
        }
    }

    private fun handleError(message: String) {
        hideLoading()
        showError(message)
    }

    private fun handleSuccess(isLoading: Boolean, artists: List<Artist>) {
        if (isLoading && artists.isNotEmpty()) {
            showLoading()
        } else {
            hideLoading()
        }
        updateList(artists)
    }

    private fun updateList(artists: List<Artist>) {
        if (artists.isEmpty()) {
            searchResultList.visibility = View.INVISIBLE
            searchResultsEmptyState.visibility = View.VISIBLE
        } else {
            searchResultList.visibility = View.VISIBLE
            searchResultsEmptyState.visibility = View.INVISIBLE
            listController.update(artists)
        }
    }

    private fun hideLoading() {
        searchResultsSwipeToRefresh.isRefreshing = false
    }

    private fun showLoading() {
        searchResultsSwipeToRefresh.isRefreshing = true
    }

    companion object {
        private const val ANIMATION_DURATION_IN_MILLIS = 400L
    }

}