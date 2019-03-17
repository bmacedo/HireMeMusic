package com.bmacedo.hirememusic.injection.modules

import com.bmacedo.hirememusic.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindings {

    @ContributesAndroidInjector
    abstract fun contributesMainActivity(): MainActivity

}