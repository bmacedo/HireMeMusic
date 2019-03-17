package com.bmacedo.hirememusic.authentication

import android.content.res.Resources
import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.bmacedo.hirememusic.R
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.coroutines.launch

class AuthenticationViewModel(
    private val authRepository: AuthenticationRepository,
    private val resources: Resources
) : ViewModel() {

    private lateinit var viewState: MutableLiveData<ViewState>

    /**
     * Starts the Spotify Authentication flow.
     * The result will be received into the caller [Fragment.onActivityResult]
     */
    fun login(fragment: Fragment): LiveData<ViewState> {
        if (!::viewState.isInitialized) {
            viewState = MutableLiveData()
        }
        if (viewState.value != ViewState.Loading) {
            viewState.postValue(ViewState.Loading)
            loginWithSpotify(fragment)
        }
        return viewState
    }

    /**
     * Called in order to continue the authentication flow by processing the result.
     */
    fun onAuthenticationResult(requestCode: Int, response: AuthenticationResponse) {
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            when (response.type) {
                AuthenticationResponse.Type.TOKEN -> handleAuthenticationSuccess(response.accessToken)
                AuthenticationResponse.Type.ERROR -> handleAuthenticationError(response.error)
                else -> handleAuthenticationCancelled()
            }
        }
    }

    private fun handleAuthenticationCancelled() {
        viewState.postValue(ViewState.Cancelled)
    }

    private fun handleAuthenticationError(error: String? = "") {
        viewState.postValue(ViewState.Error(error ?: resources.getString(R.string.generic_error)))
    }

    private fun handleAuthenticationSuccess(accessToken: String?) {
        if (accessToken.isNullOrBlank()) {
            handleAuthenticationError()
        } else {
            saveToken(accessToken)
        }
    }

    private fun loginWithSpotify(fragment: Fragment) {
        val request = authenticationRequest()
        val intent = AuthenticationClient.createLoginActivityIntent(fragment.activity, request)
        fragment.startActivityForResult(intent, AUTH_TOKEN_REQUEST_CODE)
    }

    private fun authenticationRequest(): AuthenticationRequest? {
        val clientId = resources.getString(R.string.SPOTIFY_API_KEY)
        val responseType = AuthenticationResponse.Type.TOKEN
        return AuthenticationRequest.Builder(clientId, responseType, getRedirectUri().toString())
            .setShowDialog(false)
            .build()
    }

    private fun getRedirectUri(): Uri {
        return Uri.Builder()
            .scheme(resources.getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(resources.getString(R.string.com_spotify_sdk_redirect_host))
            .build()
    }


    private fun saveToken(token: String) {
        viewModelScope.launch {
            authRepository.saveToken(token)
            viewState.postValue(ViewState.Success)
        }
    }

    /**
     * Possible states that the Authentication View might assume.
     */
    sealed class ViewState {
        object Loading : ViewState()
        object Cancelled : ViewState()
        object Success : ViewState()
        class Error(val message: String) : ViewState()
    }

    /**
     * Factory class for the ViewModel
     */
    class Factory(
        private val authRepository: AuthenticationRepository,
        private val resources: Resources
    ) : ViewModelProvider.NewInstanceFactory() {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AuthenticationViewModel::class.java)) {
                return AuthenticationViewModel(authRepository, resources) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        private const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }
}