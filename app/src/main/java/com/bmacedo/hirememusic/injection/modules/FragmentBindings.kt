package com.bmacedo.hirememusic.injection.modules

import com.bmacedo.hirememusic.authentication.AuthenticationFragment
import com.bmacedo.hirememusic.init.InitialFragment
import com.bmacedo.hirememusic.search.SearchFragment
import com.bmacedo.hirememusic.searchResults.SearchResultsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBindings {

    @ContributesAndroidInjector
    abstract fun contributesInitialFragment(): InitialFragment

    @ContributesAndroidInjector
    abstract fun contributesAuthFragment(): AuthenticationFragment

    @ContributesAndroidInjector
    abstract fun contributesSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributsSearchResultsFragment(): SearchResultsFragment
}