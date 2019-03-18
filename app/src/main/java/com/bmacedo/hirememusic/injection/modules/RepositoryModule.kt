package com.bmacedo.hirememusic.injection.modules

import android.content.SharedPreferences
import com.bmacedo.hirememusic.api.SpotifyApi
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import com.bmacedo.hirememusic.searchResults.SearchResultsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Singleton
    @Provides
    fun providesAuthenticationRepository(sharedPreferences: SharedPreferences) =
        AuthenticationRepository(preferences = sharedPreferences)

    @Singleton
    @Provides
    fun providesSearchResultsRepository(api: SpotifyApi) = SearchResultsRepository(api)
}