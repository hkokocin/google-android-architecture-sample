package com.example.hkokocin.gaa

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface BaseView {
    fun createView(layoutInflater: LayoutInflater, parent: ViewGroup?): View
}