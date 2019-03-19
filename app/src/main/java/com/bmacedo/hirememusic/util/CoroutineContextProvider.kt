package com.bmacedo.hirememusic.util

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface CoroutineContextProvider {
    val Main: CoroutineContext
    val IO: CoroutineContext

    object Impl : CoroutineContextProvider {
        override val Main: CoroutineContext by lazy { Dispatchers.Main }
        override val IO: CoroutineContext by lazy { Dispatchers.IO }
    }
}