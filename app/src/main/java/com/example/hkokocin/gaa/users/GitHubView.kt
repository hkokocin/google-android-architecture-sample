package com.example.hkokocin.gaa.users

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.adapter.WidgetAdapter

class GitHubView(
        private val adapter: WidgetAdapter,
        private val inputMethodManager: InputMethodManager,
        private val userWidgetProvider: () -> UserWidget,
        private val viewModel: UserSearchViewModel
) : LifecycleObserver {

    lateinit var root: View

    private val rvResult: RecyclerView by viewId(R.id.rv_result)
    private val etSearch: EditText by viewId(R.id.et_search)
    private val ivSearch: ImageView by viewId(R.id.iv_search)
    private val progressBar: ProgressBar by viewId(R.id.progress)

    private fun <T : View> viewId(resourcesId: Int): Lazy<T> = lazy { root.findViewById<T>(resourcesId) }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeUsers(lifecycleOwner: LifecycleOwner) = viewModel.users.observe(
            { lifecycleOwner.lifecycle },
            { adapter.setItems(it ?: emptyList()) }
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun observeProgress(lifecycleOwner: LifecycleOwner) = viewModel.showProgress.observe(
            { lifecycleOwner.lifecycle },
            { progressBar.visibility = if (it == true) View.VISIBLE else View.GONE }
    )

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initRecyclerView() {
        rvResult.layoutManager = LinearLayoutManager(root.context)
        rvResult.adapter = adapter
        adapter.addWidget(userWidgetProvider)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initSearchView() = ivSearch.setOnClickListener {
        val search = etSearch.text.toString()
        if (search.isNotBlank()) {
            viewModel.searchUsers(search)
            inputMethodManager.hideSoftInputFromWindow(etSearch.windowToken, 0)
        }
    }

    //    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    //    fun onCreate(lifecycleOwner: LifecycleOwner) {
    //
    //        val state = lifecycleOwner.lifecycle.currentState
    //        if(state == Lifecycle.State.STARTED)
    //            if(state.isAtLeast(Lifecycle.State.CREATED))
    //
    //                viewModel.users.observe(
    //                        { lifecycleOwner.lifecycle },
    //                        { adapter.setItems(it ?: emptyList()) }
    //                )
    //
    //        viewModel.showProgress.observe(
    //                { lifecycleOwner.lifecycle },
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
}