package com.bmacedo.hirememusic.authentication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment
import com.bmacedo.hirememusic.util.navigateSafe
import com.bmacedo.hirememusic.util.observe
import com.spotify.sdk.android.authentication.AuthenticationClient
import kotlinx.android.synthetic.main.fragment_authentication.*
import timber.log.Timber
import javax.inject.Inject

@ContentView(R.layout.fragment_authentication)
class AuthenticationFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: AuthenticationViewModel.Factory

    private val viewModel: AuthenticationViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(AuthenticationViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginButton.setOnClickListener {
            viewModel.login(this).observe(this) { viewState ->
                when (viewState) {
                    AuthenticationViewModel.ViewState.Loading -> handleAuthenticationLoading()
                    AuthenticationViewModel.ViewState.Cancelled -> handleAuthenticationCancelled()
                    is AuthenticationViewModel.ViewState.Success -> handleAuthenticationSuccess()
                    is AuthenticationViewModel.ViewState.Error -> handleAuthenticationError(viewState.message)
                    null -> IllegalStateException("Authentication State should not be null")
                }
            }
        }
    }

    private fun handleAuthenticationLoading() {
        loading.show()
        loginButton.visibility = View.INVISIBLE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)
        viewModel.onAuthenticationResult(requestCode, response)
    }


    private fun handleAuthenticationSuccess() {
        findNavController().navigateSafe(AuthenticationFragmentDirections.actionAuthenticationFragmentToSearchFragment())
    }

    private fun handleAuthenticationError(error: String?) {
        Timber.d(error)
        loading.hide()
        loginButton.visibility = View.VISIBLE
    }

    private fun handleAuthenticationCancelled() {
        loading.hide()
        loginButton.visibility = View.VISIBLE
    }

}