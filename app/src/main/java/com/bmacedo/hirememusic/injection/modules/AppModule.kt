package com.bmacedo.hirememusic.injection.modules

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class, ViewModelModule::class])
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesContext(): Context = application

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Singleton
    fun providesResources(): Resources = application.resources

    @Provides
    @Singleton
    fun providesSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

}