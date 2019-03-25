package com.bmacedo.hirememusic

import com.bmacedo.hirememusic.util.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext


object AndroidTestCoroutineContext : CoroutineContextProvider {
    @ExperimentalCoroutinesApi
    override val Main: CoroutineContext = Dispatchers.Main

    @ExperimentalCoroutinesApi
    override val IO: CoroutineContext = Dispatchers.Main
}