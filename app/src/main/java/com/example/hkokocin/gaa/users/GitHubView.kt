package com.example.hkokocin.gaa.users

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.hkokocin.gaa.BaseView
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.adapter.WidgetAdapter

class GitHubView(
        private val adapter: WidgetAdapter,
        private val inputMethodManager: InputMethodManager,
        private val userWidgetProvider: () -> UserWidget,
        private val lifecycle: Lifecycle,
        private val viewModel: UserSearchViewModel
) : BaseView(), LifecycleObserver {

    private val rvResult: RecyclerView by viewId(R.id.rv_result)
    private val etSearch: EditText by viewId(R.id.et_search)
    private val ivSearch: ImageView by viewId(R.id.iv_search)
    private val progressBar: ProgressBar by viewId(R.id.progress)

//    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
//    fun onCreate() {
//
//        viewModel.users.observe(
//                { lifecycle },
//                { adapter.setItems(it ?: emptyList()) }
//        )
//
//        viewModel.showProgress.observe(
//                { lifecycle },
//                { progressBar.visibility = if (it == true) View.VISIBLE else View.GONE }
//        )
//
//        ivSearch.setOnClickListener {
//            val search = etSearch.text.toString()
//            if (search.isNotBlank()) {
//                viewModel.searchUsers(search)
//                inputMethodManager.hideSoftInputFromWindow(etSearch.windowToken, 0)
//            }
//        }
//
//        rvResult.layoutManager = LinearLayoutManager(root.context)
//        rvResult.adapter = adapter
//        adapter.addWidget(userWidgetProvider)
//    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeUsers() {
        viewModel.users.observe(
                { lifecycle },
                { adapter.setItems(it ?: emptyList()) }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeProgress() {
        viewModel.showProgress.observe(
                { lifecycle },
                { progressBar.visibility = if (it == true) View.VISIBLE else View.GONE }
        )
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initRecyclerView() {
        rvResult.layoutManager = LinearLayoutManager(root.context)
        rvResult.adapter = adapter
        adapter.addWidget(userWidgetProvider)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initSearchView() {
        ivSearch.setOnClickListener {
            val search = etSearch.text.toString()
            if (search.isNotBlank()) {
                viewModel.searchUsers(search)
                inputMethodManager.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }
    }
}