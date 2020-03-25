package com.vitoksmile.tools.db.explorer.app.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LiveData<T>.bind(fragment: Fragment, crossinline observer: (value: T) -> Unit) {
    observe(fragment.viewLifecycleOwner, Observer { observer(it) })
}
