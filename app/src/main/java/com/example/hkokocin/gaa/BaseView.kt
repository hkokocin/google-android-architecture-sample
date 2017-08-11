package com.example.hkokocin.gaa

import android.view.View

abstract class BaseView {
    lateinit var root: View

    protected fun <T : View> viewId(resourcesId: Int): Lazy<T> = lazy { root.findViewById<T>(resourcesId) }
}