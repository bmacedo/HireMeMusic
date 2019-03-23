package com.bmacedo.hirememusic.injection.modules

import android.content.res.Resources
import com.bmacedo.hirememusic.authentication.AuthenticationRepository
import com.bmacedo.hirememusic.authentication.AuthenticationViewModel
import com.bmacedo.hirememusic.init.InitialViewModel
import com.bmacedo.hirememusic.searchResults.SearchResultsRepository
import com.bmacedo.hirememusic.searchResults.SearchResultsViewModel
import com.bmacedo.hirememusic.util.CoroutineContextProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {

    @Provides
    @Singleton
    fun providesInitialViewModelFactory(
        authenticationRepository: AuthenticationRepository
    ): InitialViewModel.Factory {
        return InitialViewModel.Factory(authenticationRepository)
    }

    @Provides
    @Singleton
    fun providesAuthenticationViewModelFactory(
        authenticationRepository: AuthenticationRepository,
        coroutineContextProvider: CoroutineContextProvider,
        resources: Resources
    ): AuthenticationViewModel.Factory {
        return AuthenticationViewModel.Factory(authenticationRepository, coroutineContextProvider, resources)
    }

    @Provides
    @Singleton
    fun providesSearchResultsViewModelFactory(
        searchResultsRepository: SearchResultsRepository,
        authenticationRepository: AuthenticationRepository,
        resources: Resources
    ): SearchResultsViewModel.Factory {
        return SearchResultsViewModel.Factory(searchResultsRepository, authenticationRepository, resources)
    }

}