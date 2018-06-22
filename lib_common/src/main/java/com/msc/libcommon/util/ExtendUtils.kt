package com.msc.libcommon.util

import android.support.design.widget.BaseTransientBottomBar
import android.support.design.widget.Snackbar
import android.view.View
import com.google.gson.Gson


/**
 * Show a snackbar with [message], execute [f] and show it
 */
inline fun View.snack(message: String, @BaseTransientBottomBar.Duration length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}