package com.bmacedo.hirememusic.searchResults

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.transition.ChangeBounds
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment


class SearchResultsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        sharedElementEnterTransition = ChangeBounds().apply {
            duration = 750
        }
        sharedElementReturnTransition = ChangeBounds().apply {
            duration = 750
        }
        return inflater.inflate(R.layout.fragment_search_results, container, false)
    }

}