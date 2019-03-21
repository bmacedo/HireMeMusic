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
        setLoginButtonClickListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)
        viewModel.onAuthenticationResult(requestCode, response)
    }

    private fun setLoginButtonClickListener() {
        authLoginButton.setOnClickListener {
            viewModel.login(this).observe(this) { viewState ->
                when (viewState) {
                    AuthenticationViewModel.ViewState.Loading -> showLoading()
                    AuthenticationViewModel.ViewState.Cancelled -> handleAuthenticationCancelled()
                    is AuthenticationViewModel.ViewState.Success -> handleAuthenticationSuccess()
                    is AuthenticationViewModel.ViewState.Error -> handleAuthenticationError(viewState.message)
                    null -> IllegalStateException("Authentication State should not be null")
                }
            }
        }
    }

    private fun handleAuthenticationCancelled() {
        showSnackMessage(getString(R.string.log_in_cancelled))
        hideLoading()
    }

    private fun handleAuthenticationSuccess() {
        findNavController().navigateSafe(AuthenticationFragmentDirections.actionAuthenticationFragmentToSearchFragment())
    }

    private fun handleAuthenticationError(error: String?) {
        Timber.d(error)
        error?.let { showSnackMessage(error) }
        hideLoading()
    }

    private fun showLoading() {
        authLoading.show()
        authLoginButton.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        authLoading.hide()
        authLoginButton.visibility = View.VISIBLE
    }

}