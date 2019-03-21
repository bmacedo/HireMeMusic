package com.bmacedo.hirememusic.search

import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment
import com.bmacedo.hirememusic.util.navigateSafe
import kotlinx.android.synthetic.main.fragment_search.*

@ContentView(R.layout.fragment_search)
class SearchFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchBarClickListener()
    }

    private fun setSearchBarClickListener() {
        searchBar.setOnClickListener {
            navigateToSearchResults()
        }
    }

    private fun navigateToSearchResults() {
        val extras = FragmentNavigatorExtras(searchBar to "searchResultTransition")
        findNavController().navigateSafe(
            SearchFragmentDirections.actionSearchFragmentToSearchResultsFragment(),
            extras
        )
    }
}