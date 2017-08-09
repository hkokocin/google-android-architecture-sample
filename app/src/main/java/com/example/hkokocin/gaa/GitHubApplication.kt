package com.example.hkokocin.gaa

import android.app.Application


class GitHubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initApplicationScope()
    }
}