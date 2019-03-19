package com.bmacedo.hirememusic.authentication

import android.content.res.Resources
import com.nhaarman.mockito_kotlin.mock
import org.junit.Before
import org.junit.Test

class AuthenticationViewModelTest {

    private val authenticationRepository: AuthenticationRepository = mock()
    private val resources: Resources = mock()

    private lateinit var viewModel: AuthenticationViewModel

    @Before
    fun setUp() {
        viewModel = AuthenticationViewModel(authenticationRepository, resources)
    }

    @Test
    fun login_whenFirstInit_callsFragmentStartForResult() {
        // TODO
    }
}