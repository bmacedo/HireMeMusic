package com.bmacedo.hirememusic.authentication

import android.content.res.Resources
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.bmacedo.hirememusic.TestCoroutineContext
import com.jraska.livedata.test
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AuthenticationViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val fragment: Fragment = mock()
    private val authenticationRepository: AuthenticationRepository = mock()
    private val resources: Resources = mock()

    private lateinit var viewModel: AuthenticationViewModel

    @Before
    fun setUp() {
        val activityMock: FragmentActivity = mock()
        whenever(fragment.activity).thenReturn(activityMock)
        whenever(resources.getString(any())).thenReturn("test")
        viewModel = AuthenticationViewModel(authenticationRepository, TestCoroutineContext, resources)
    }

    @Test
    fun login_whenFirstInit_callsFragmentStartForResult() {
        viewModel.login(fragment, "test://callback")
        verify(fragment).startActivityForResult(any(), any())
    }

    @Test
    fun login_whenFirstInit_postLoading() {
        val observer = viewModel.login(fragment, "test://callback").test()
        observer.assertHasValue()
            .assertValue(AuthenticationViewModel.ViewState.Loading)
    }

    @Test
    fun onAuthenticationResult_whenError_postErrorMessage() {
        val observer = viewModel.login(fragment, "test://callback").test()

        val response = AuthenticationResponse.Builder()
            .setType(AuthenticationResponse.Type.ERROR)
            .setError("testError")
            .build()

        viewModel.onAuthenticationResult(AuthenticationViewModel.AUTH_TOKEN_REQUEST_CODE, response)

        observer.assertHasValue()
            .assertValue { viewState ->
                viewState is AuthenticationViewModel.ViewState.Error && viewState.message == "testError"
            }
    }

    @Test
    fun onAuthenticationResult_whenCancelled_postCancellation() {
        val observer = viewModel.login(fragment, "test://callback").test()

        val response = AuthenticationResponse.Builder()
            .setType(AuthenticationResponse.Type.EMPTY)
            .build()

        viewModel.onAuthenticationResult(AuthenticationViewModel.AUTH_TOKEN_REQUEST_CODE, response)

        observer.assertHasValue().assertValue(AuthenticationViewModel.ViewState.Cancelled)
    }

    @Test
    fun onAuthenticationResult_whenTokenObtained_postSuccess() {
        val observer = viewModel.login(fragment, "test://callback").test()

        val response = AuthenticationResponse.Builder()
            .setType(AuthenticationResponse.Type.TOKEN)
            .setAccessToken("testToken")
            .build()

        viewModel.onAuthenticationResult(AuthenticationViewModel.AUTH_TOKEN_REQUEST_CODE, response)

        observer.assertHasValue()
            .assertValue(AuthenticationViewModel.ViewState.Success)
    }

    @Test
    fun onAuthenticationResult_whenTokenObtained_persistToken() {
        runBlocking {
            viewModel.login(fragment, "test://callback")

            val response = AuthenticationResponse.Builder()
                .setType(AuthenticationResponse.Type.TOKEN)
                .setAccessToken("testToken")
                .build()

            viewModel.onAuthenticationResult(AuthenticationViewModel.AUTH_TOKEN_REQUEST_CODE, response)

            verify(authenticationRepository).saveToken("testToken")
        }
    }
}