package com.bmacedo.hirememusic.base

import android.content.Context
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection

abstract class BaseFragment : Fragment() {

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    protected fun showError(errorMessage: String) {
        view?.let { view ->
            Snackbar.make(view, errorMessage, Snackbar.LENGTH_SHORT).show()
        }
    }
}