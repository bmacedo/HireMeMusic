package com.bmacedo.hirememusic.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observe(fragment: Fragment, body: (T?) -> Unit) {
    this.observe(fragment, Observer(body))
}