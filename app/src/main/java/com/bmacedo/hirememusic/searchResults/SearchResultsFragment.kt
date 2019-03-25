package com.bmacedo.hirememusic.searchResults

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment
import com.bmacedo.hirememusic.searchResults.model.Artist
import com.bmacedo.hirememusic.util.navigateSafe
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
    private var dialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = ANIMATION_DURATION_IN_MILLIS
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = ANIMATION_DURATION_IN_MILLIS
        }
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpList()
        observeQueryChange()
        showKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialog?.dismiss()
    }

    private fun observeViewState() {
        viewModel.viewState().observe(this) { viewState ->
            refreshUi(viewState)
        }
    }

    private fun observeQueryChange() {
        searchButton.setOnClickListener {
            searchArtist(searchField.text.toString())
        }
        searchField.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchArtist(textView.text.toString())
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun searchArtist(query: String) {
        viewModel.queryArtist(query)
        hideKeyboard()
    }

    private fun setUpList() {
        searchResultList.layoutManager = LinearLayoutManager(context)
        searchResultList.adapter = listController.adapter
        searchResultsSwipeToRefresh.setOnRefreshListener {
            viewModel.resetSearch()
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
        hideKeyboard()
        context?.let { ctx ->
            dialog = AlertDialog.Builder(ctx)
                .setMessage(com.bmacedo.hirememusic.R.string.authentication_error_message)
                .setPositiveButton(R.string.ok) { _, _ ->
                    navigateToLogin()
                }
                .create()
            dialog?.show()
        }
    }

    private fun navigateToLogin() {
        findNavController().navigateSafe(SearchResultsFragmentDirections.actionSearchResultsFragmentToAuthenticationFragment())
    }

    private fun handleError(message: String) {
        hideLoading()
        showSnackMessage(message)
    }

    private fun handleSuccess(isLoading: Boolean, artists: List<Artist>) {
        if (isLoading) {
            showLoading()
        } else {
            hideLoading()
            updateList(artists)
        }
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

    private fun showKeyboard() {
        val imgr = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imgr?.showSoftInput(searchField, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideKeyboard() {
        val imgr = activity?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imgr?.hideSoftInputFromWindow(view?.windowToken, 0)
    }

    companion object {
        private const val ANIMATION_DURATION_IN_MILLIS = 400L
    }

}