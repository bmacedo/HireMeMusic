package com.bmacedo.hirememusic

import android.app.Application
import timber.log.Timber

class HireMeMusic : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}