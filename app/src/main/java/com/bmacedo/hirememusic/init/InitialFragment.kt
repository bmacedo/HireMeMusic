package com.bmacedo.hirememusic.init

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import androidx.annotation.ContentView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import com.bmacedo.hirememusic.util.observe
import kotlinx.android.synthetic.main.fragment_initial.*

@ContentView(R.layout.fragment_initial)
class InitialFragment : Fragment() {

    //TODO @Inject
    private val viewModelFactory: InitialViewModel.Factory by lazy {
        InitialViewModel.Factory(AuthenticationRepository(PreferenceManager.getDefaultSharedPreferences(context)))
    }

    private val viewModel: InitialViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InitialViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoggedIn().observe(this) { viewState ->
            when (viewState) {
                InitialViewModel.ViewState.LOADING -> loading.visibility = View.VISIBLE
                InitialViewModel.ViewState.LOGGED_IN -> navigateToSearch()
                InitialViewModel.ViewState.LOGGED_OUT -> navigateToAuthentication()
                null -> throw IllegalStateException("InitialViewState should not be null")
            }
        }
    }

    private fun navigateToAuthentication() {
        findNavController().navigate(InitialFragmentDirections.actionInitialFragmentToAuthenticationFragment())
    }

    private fun navigateToSearch() {
        findNavController().navigate(InitialFragmentDirections.actionInitialFragmentToSearchFragment())
    }

}
