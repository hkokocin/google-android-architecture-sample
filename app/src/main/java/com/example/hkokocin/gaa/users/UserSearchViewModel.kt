package com.example.hkokocin.gaa.users

import android.arch.lifecycle.ViewModel
import android.databinding.*
import android.support.v7.widget.RecyclerView
import android.text.Editable
import com.example.hkokocin.gaa.BR
import com.example.hkokocin.gaa.adapter.WidgetAdapter
import com.example.hkokocin.gaa.data.GitHubRepository
import com.example.hkokocin.gaa.data.GitHubUser
import com.example.hkokocin.gaa.data.UserSearchResult
import io.reactivex.BackpressureStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit


class UserSearchViewModel(
        private val repository: GitHubRepository
) : ViewModel() {

    val users = ObservableField<List<GitHubUser>>(emptyList())
    val showProgress = ObservableField<Boolean>(false)

    val searchSubject = PublishSubject.create<String>()
    val searchFlowable = searchSubject
        .toFlowable(BackpressureStrategy.LATEST)
        .debounce(500, TimeUnit.MILLISECONDS)
        .doOnNext { showProgress.set(true) }
        .flatMap {
            repository.searchUser(it)
                .subscribeOn(Schedulers.io())
                .onErrorReturn { UserSearchResult(emptyList()) }
        }
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext { showProgress.set(false) }
        .subscribe { (items) -> users.set(items) }

    fun searchChanged(text: Editable) {
        searchSubject.onNext(text.toString())
    }

    override fun onCleared() {
        super.onCleared()
        searchFlowable.dispose()
    }
}

@BindingAdapter("bind:items")
@Suppress("UNCHECKED_CAST")
fun bindRecyclerViewItems(view: RecyclerView, items: List<*>) {
    (view.adapter as? WidgetAdapter)?.setItems(items as List<Any>)
}