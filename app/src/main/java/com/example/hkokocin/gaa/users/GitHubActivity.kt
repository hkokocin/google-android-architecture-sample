package com.example.hkokocin.gaa.users

import android.arch.lifecycle.LifecycleActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.hkokocin.gaa.adapter.WidgetAdapter
import com.example.hkokocin.gaa.databinding.GithubActivityBinding
import com.example.hkokocin.gaa.gitHubActivityScope
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.provider

class GitHubActivity : LifecycleActivity() {

    private val injector = gitHubActivityScope(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = GithubActivityBinding.inflate(layoutInflater)
        binding.viewModel = injector.instance()

        setContentView(binding.root)
        initRecyclerView(binding.rvResult)
    }

    private fun initRecyclerView(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(this)

        val adapter: WidgetAdapter = injector.instance()
        recyclerView.adapter = adapter
        adapter.addWidget(injector.provider<UserWidget>())
    }
}
