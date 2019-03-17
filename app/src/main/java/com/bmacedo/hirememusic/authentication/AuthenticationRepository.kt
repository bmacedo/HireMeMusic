package com.bmacedo.hirememusic.authentication

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthenticationRepository(private val preferences: SharedPreferences) {

    /**
     * Returns true if the user has a valid access token
     */
    suspend fun isLoggedIn(): Boolean =
        withContext(Dispatchers.IO) {
            val token = preferences.getString(ACCESS_TOKEN_KEY, "")
            !token.isNullOrBlank()
        }

    /**
     * Store the user access token for further accesses
     */
    suspend fun saveToken(token: String) {
        withContext(Dispatchers.IO) {
            preferences.edit {
                putString(ACCESS_TOKEN_KEY, token)
            }
        }
    }

    companion object {
        private const val ACCESS_TOKEN_KEY = "spotify_access_token"
    }
}