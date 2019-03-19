package com.bmacedo.hirememusic

import com.bmacedo.hirememusic.util.CoroutineContextProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlin.coroutines.CoroutineContext


object TestCoroutineContext : CoroutineContextProvider {
    @ExperimentalCoroutinesApi
    override val Main: CoroutineContext = Dispatchers.Unconfined

    @ExperimentalCoroutinesApi
    override val IO: CoroutineContext = Dispatchers.Unconfined
}