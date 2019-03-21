package com.bmacedo.hirememusic.init

import android.os.Bundle
import android.view.View
import androidx.annotation.ContentView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bmacedo.hirememusic.R
import com.bmacedo.hirememusic.base.BaseFragment
import com.bmacedo.hirememusic.util.navigateSafe
import com.bmacedo.hirememusic.util.observe
import kotlinx.android.synthetic.main.fragment_initial.*
import javax.inject.Inject

@ContentView(R.layout.fragment_initial)
class InitialFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: InitialViewModel.Factory

    private val viewModel: InitialViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(InitialViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewState()
    }

    private fun observeViewState() {
        viewModel.isLoggedIn().observe(this) { viewState ->
            when (viewState) {
                InitialViewModel.ViewState.LOADING -> showLoading()
                InitialViewModel.ViewState.LOGGED_IN -> navigateToSearch()
                InitialViewModel.ViewState.LOGGED_OUT -> navigateToAuthentication()
                null -> throw IllegalStateException("InitialViewState should not be null")
            }
        }
    }

    private fun showLoading() {
        initialLoading.show()
    }

    private fun navigateToSearch() {
        findNavController().navigateSafe(InitialFragmentDirections.actionInitialFragmentToSearchFragment())
    }

    private fun navigateToAuthentication() {
        findNavController().navigateSafe(InitialFragmentDirections.actionInitialFragmentToAuthenticationFragment())
    }

}
