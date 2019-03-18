package com.bmacedo.hirememusic.api

import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import okhttp3.Interceptor
import okhttp3.Response


class AuthorizationInterceptor(private val authenticationRepository: AuthenticationRepository) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val mainRequest = chain.request()
        val builder = mainRequest.newBuilder()
            .header("Authorization", getAuthorization())
            .method(mainRequest.method(), mainRequest.body())
        return chain.proceed(builder.build())
    }

    private fun getAuthorization() = "Bearer " + authenticationRepository.getToken()
}