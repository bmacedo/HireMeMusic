package com.bmacedo.hirememusic.util

import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference


class EditTextDebounce private constructor(editText: EditText) {
    private val editTextWeakReference: WeakReference<EditText>?
    private val debounceHandler: Handler = Handler(Looper.getMainLooper())
    private var debounceCallback: DebounceCallback? = null
    private var debounceWorker: Runnable? = null
    private var delayMillis: Long = 0L
    private val textWatcher: TextWatcher

    init {
        this.debounceWorker = DebounceRunnable("", null)
        this.delayMillis = 300
        this.textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //unused
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //unused
            }

            override fun afterTextChanged(s: Editable) {
                debounceHandler.removeCallbacks(debounceWorker)
                debounceWorker = DebounceRunnable(s.toString(), debounceCallback)
                debounceHandler.postDelayed(debounceWorker, delayMillis)
            }
        }
        this.editTextWeakReference = WeakReference(editText)
        editTextWeakReference.get()?.addTextChangedListener(textWatcher)
    }

    fun watch(debounceCallback: DebounceCallback) {
        this.debounceCallback = debounceCallback
    }

    fun watch(debounceCallback: DebounceCallback, delayMillis: Long) {
        this.debounceCallback = debounceCallback
        this.delayMillis = delayMillis
    }

    fun unwatch() {
        if (editTextWeakReference != null) {
            val editText = editTextWeakReference.get()
            if (editText != null) {
                editText.removeTextChangedListener(textWatcher)
                editTextWeakReference.clear()
                debounceHandler.removeCallbacks(debounceWorker)
            }
        }
    }

    private fun setDelayMillis(delayMillis: Long) {
        this.delayMillis = delayMillis
    }

    private class DebounceRunnable internal constructor(
        private val result: String,
        private val debounceCallback: DebounceCallback?
    ) : Runnable {

        override fun run() {
            debounceCallback?.onFinished(result)
        }
    }

    interface DebounceCallback {
        fun onFinished(result: String)
    }

    companion object {

        fun create(editText: EditText): EditTextDebounce {
            return EditTextDebounce(editText)
        }

        fun create(editText: EditText, delayMillis: Long): EditTextDebounce {
            val editTextDebounce = EditTextDebounce(editText)
            editTextDebounce.setDelayMillis(delayMillis)
            return editTextDebounce
        }
    }

}