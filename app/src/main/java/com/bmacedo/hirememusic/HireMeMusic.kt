package com.bmacedo.hirememusic

import android.app.Activity
import android.app.Application
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import com.bmacedo.hirememusic.injection.components.AppComponent
import com.bmacedo.hirememusic.injection.components.DaggerAppComponent
import com.bmacedo.hirememusic.injection.modules.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class HireMeMusic : Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @set:VisibleForTesting
    lateinit var component: AppComponent

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = androidInjector

    override fun onCreate() {
        super.onCreate()
        initLogging()
        initDependencyInjection()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initDependencyInjection() {
        component = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()

        component.inject(this)
    }
}