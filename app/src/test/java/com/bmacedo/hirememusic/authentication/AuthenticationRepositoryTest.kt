package com.bmacedo.hirememusic.authentication

import android.content.SharedPreferences
import com.bmacedo.hirememusic.TestCoroutineContext
import com.nhaarman.mockito_kotlin.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthenticationRepositoryTest {

    private val editor: SharedPreferences.Editor = mock()
    private val preferences: SharedPreferences = mock()

    private lateinit var repository: AuthenticationRepository

    @Before
    fun setUp() {
        repository = AuthenticationRepository(preferences, TestCoroutineContext)
    }

    @Test
    fun isLoggedIn_whenTokenIsNull_returnsFalse() {
        runBlocking {
            whenever(preferences.getString(any(), any())).thenReturn(null)
            assertFalse(repository.isLoggedIn())
        }
    }

    @Test
    fun isLoggedIn_whenTokenIsEmpty_returnsFalse() {
        runBlocking {
            whenever(preferences.getString(any(), any())).thenReturn("")
            assertFalse(repository.isLoggedIn())
        }
    }

    @Test
    fun isLoggedIn_whenTokenIsFilled_returnsTrue() {
        runBlocking {
            whenever(preferences.getString(any(), any())).thenReturn("testToken")
            assertTrue(repository.isLoggedIn())
        }
    }

    @Test
    fun getToken_whenTokenIsEmpty_returnsEmpty() {
        runBlocking {
            whenever(preferences.getString(any(), any())).thenReturn("")
            assertEquals("", repository.getToken())
        }
    }

    @Test
    fun getToken_whenTokenIsFilled_returnsToken() {
        runBlocking {
            whenever(preferences.getString(any(), any())).thenReturn("testToken")
            assertEquals("testToken", repository.getToken())
        }
    }

    @Test
    fun saveToken_savesToPreferences() {
        whenever(preferences.edit()).thenReturn(editor)
        runBlocking {
            repository.saveToken("testToken")
            verify(editor, times(1)).putString(any(), eq("testToken"))
        }
    }
}