package com.bmacedo.hirememusic.injection.components

import android.app.Application
import com.bmacedo.hirememusic.HireMeMusic
import com.bmacedo.hirememusic.injection.modules.ActivityBindings
import com.bmacedo.hirememusic.injection.modules.AppModule
import com.bmacedo.hirememusic.injection.modules.FragmentBindings
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Main dependency injection component
 */
@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ActivityBindings::class,
        FragmentBindings::class
    ]
)
interface AppComponent : AndroidInjector<HireMeMusic> {

    fun inject(application: Application)

}