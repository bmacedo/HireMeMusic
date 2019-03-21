package com.bmacedo.hirememusic.authentication

import android.content.SharedPreferences
import androidx.core.content.edit
import com.bmacedo.hirememusic.util.CoroutineContextProvider
import kotlinx.coroutines.withContext

class AuthenticationRepository(
    private val preferences: SharedPreferences,
    private val coroutineContextProvider: CoroutineContextProvider
) {

    /**
     * Returns true if the user has a valid access token
     */
    suspend fun isLoggedIn(): Boolean =
        withContext(coroutineContextProvider.IO) {
            val token = getToken()
            !token.isNullOrBlank()
        }

    /**
     * Stores the user access token for further accesses
     */
    suspend fun saveToken(token: String) {
        withContext(coroutineContextProvider.IO) {
            preferences.edit {
                putString(ACCESS_TOKEN_KEY, token)
            }
        }
    }

    /**
     * Removes the stored access token
     */
    suspend fun invalidateToken() {
        saveToken("")
    }

    /**
     * Retrieves the current access token
     */
    fun getToken(): String? = preferences.getString(ACCESS_TOKEN_KEY, "")

    companion object {
        private const val ACCESS_TOKEN_KEY = "spotify_access_token"
    }
}