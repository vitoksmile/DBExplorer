package com.vitoksmile.tools.db.explorer.app.extensions

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@Suppress("NOTHING_TO_INLINE")
inline fun ViewGroup.inflate(@LayoutRes resId: Int, attachToRoot: Boolean = false): View =
    LayoutInflater.from(context).inflate(resId, this, attachToRoot)
