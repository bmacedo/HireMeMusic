package com.bmacedo.hirememusic.init

import androidx.lifecycle.*
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import kotlinx.coroutines.launch

/**
 * ViewModel associated with the InitialView
 */
class InitialViewModel(private val authRepository: AuthenticationRepository) : ViewModel() {

    private lateinit var authenticationState: MutableLiveData<ViewState>

    /**
     * Returns a LiveData with the authentication result.
     */
    fun isLoggedIn(): LiveData<ViewState> {
        if (!::authenticationState.isInitialized) {
            authenticationState = MutableLiveData()
            authenticationState.postValue(ViewState.LOADING)
            viewModelScope.launch {
                if (authRepository.isLoggedIn()) {
                    authenticationState.postValue(ViewState.LOGGED_IN)
                } else {
                    authenticationState.postValue(ViewState.LOGGED_OUT)
                }
            }
        }
        return authenticationState
    }

    /**
     * Contains the possible states that the InitialView can assume
     */
    enum class ViewState {
        LOADING,
        LOGGED_IN,
        LOGGED_OUT
    }

    /**
     * Factory class for the ViewModel
     */
    class Factory(private val authRepository: AuthenticationRepository) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(InitialViewModel::class.java)) {
                return InitialViewModel(authRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}