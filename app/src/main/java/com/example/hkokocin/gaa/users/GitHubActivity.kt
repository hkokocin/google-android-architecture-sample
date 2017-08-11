package com.example.hkokocin.gaa.users

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.gitHubActivityScope
import com.github.salomonbrys.kodein.instance

class GitHubActivity : LifecycleActivity() {

    private val injector = gitHubActivityScope(this)
    private val view: GitHubView by lazy { injector.instance<GitHubView>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = layoutInflater.inflate(R.layout.github_activity, null)
        setContentView(root)

        view.root = root
        lifecycle.addObserver(view)
    }
}
