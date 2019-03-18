package com.bmacedo.hirememusic.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SearchResultsViewModel(private val searchResultsRepository: SearchResultsRepository) : ViewModel() {


    /**
     * Factory class for the ViewModel
     */
    class Factory(private val searchResultsRepository: SearchResultsRepository) :
        ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchResultsViewModel::class.java)) {
                return SearchResultsViewModel(searchResultsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}