package com.bmacedo.hirememusic

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.spotify.sdk.android.authentication.AuthenticationClient
import com.spotify.sdk.android.authentication.AuthenticationRequest
import com.spotify.sdk.android.authentication.AuthenticationResponse
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loginButton.setOnClickListener { loginWithSpotify() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val response = AuthenticationClient.getResponse(resultCode, data)

        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            Toast.makeText(this, response.accessToken, Toast.LENGTH_LONG).show()
            Timber.d(response.error)
        }
    }

    private fun loginWithSpotify() {
        val clientId = getString(R.string.SPOTIFY_API_KEY)
        val responseType = AuthenticationResponse.Type.TOKEN
        val request = AuthenticationRequest.Builder(clientId, responseType, getRedirectUri().toString())
            .setShowDialog(true)
            .build()
        AuthenticationClient.openLoginActivity(this, AUTH_TOKEN_REQUEST_CODE, request)
    }

    private fun getRedirectUri(): Uri {
        return Uri.Builder()
            .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
            .authority(getString(R.string.com_spotify_sdk_redirect_host))
            .build()
    }

    companion object {
        private const val AUTH_TOKEN_REQUEST_CODE = 0x10
    }
}
