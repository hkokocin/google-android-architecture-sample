package com.example.hkokocin.gaa.users

import android.app.Activity
import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.adapter.WidgetAdapter
import com.example.hkokocin.gaa.gitHubActivityScope
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance

class GitHubActivity : LifecycleActivity() {

    private val injector = gitHubActivityScope(this)
    private val adapter by lazy { injector.instance<WidgetAdapter>() }
    private val inputMethodManager by lazy { injector.instance<InputMethodManager>() }

    private val rvResult by lazy { findViewById<RecyclerView>(R.id.rv_result) }
    private val etSearch by lazy { findViewById<EditText>(R.id.et_search) }
    private val ivSearch by lazy { findViewById<ImageView>(R.id.iv_search) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.github_activity)
        initRecyclerView()

        val viewModel = ViewModelProviders
            .of(this, ViewModelFactory(injector))
            .get(UserSearchViewModel::class.java)

        viewModel.users.observe(
                { lifecycle },
                { adapter.setItems(it ?: emptyList()) }
        )

        viewModel.showProgress.observe(
                { lifecycle },
                { progressBar.visibility = if (it == true) View.VISIBLE else View.GONE }
        )

        ivSearch.setOnClickListener {
            val search = etSearch.text.toString()
            if (search.isNotBlank()) {
                viewModel.searchUsers(search)
                inputMethodManager.hideSoftInputFromWindow(etSearch.windowToken, 0)
            }
        }
    }

    private fun initRecyclerView() {
        rvResult.layoutManager = LinearLayoutManager(this)
        rvResult.adapter = adapter
        adapter.addWidget { injector.instance<UserWidget>() }
    }
}

fun <T : View> Activity.viewId(resourcesId: Int): Lazy<T> = lazy { findViewById<T>(resourcesId) }

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val injector: Kodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        UserSearchViewModel::class.java -> injector.instance<UserSearchViewModel>()
        else                            -> throw IllegalAccessException("unable to create ${modelClass.name}")
    } as T
}
