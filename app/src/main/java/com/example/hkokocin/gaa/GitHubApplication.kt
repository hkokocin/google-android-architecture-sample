package com.example.hkokocin.gaa

import android.app.Application
import com.squareup.leakcanary.LeakCanary


class GitHubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initApplicationScope()

        if (!LeakCanary.isInAnalyzerProcess(this))
            LeakCanary.install(this);
    }
}