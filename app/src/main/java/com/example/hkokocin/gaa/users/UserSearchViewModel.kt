package com.example.hkokocin.gaa.users

import android.arch.lifecycle.ViewModel
import android.databinding.Bindable
import android.databinding.BindingAdapter
import android.databinding.Observable
import android.databinding.PropertyChangeRegistry
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
) : ObservableViewModel() {

    @get:Bindable
    var users = listOf<GitHubUser>()
        set(value) {
            field = value
            notifyFieldChanged(BR.users)
        }

    @get:Bindable
    var showProgress = false
        set(value) {
            field = value
            notifyFieldChanged(BR.showProgress)
        }

    val searchSubject = PublishSubject.create<String>()
    val searchFlowable = searchSubject
            .toFlowable(BackpressureStrategy.LATEST)
            .debounce(500, TimeUnit.MILLISECONDS)
            .doOnNext { showProgress = true }
            .flatMap {
                repository.searchUser(it)
                        .subscribeOn(Schedulers.io())
                        .doOnError { it.printStackTrace() }
                        .onErrorReturn { UserSearchResult(emptyList()) }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnError { it.printStackTrace() }
            .onErrorReturn { UserSearchResult(emptyList()) }
            .subscribe { (items) ->
                users = items
                showProgress = false
            }


    fun searchChanged(text: Editable) {
        searchSubject.onNext(text.toString())
    }

    override fun onCleared() {
        super.onCleared()
        searchFlowable.dispose()
    }
}

// liveData.postValue() instead of observeOn()
typealias Callback = Observable.OnPropertyChangedCallback

abstract class ObservableViewModel : ViewModel(), Observable {
    @Transient private var callbacks = PropertyChangeRegistry()

    override fun addOnPropertyChangedCallback(callback: Callback) = callbacks.add(callback)
    override fun removeOnPropertyChangedCallback(callback: Callback) = callbacks.remove(callback)

    protected fun notifyFieldChanged(fieldId: Int) = callbacks.notifyCallbacks(this, fieldId, null)

    override fun onCleared() {
        super.onCleared()
        callbacks.clear()
    }
}

@BindingAdapter("bind:items")
@Suppress("UNCHECKED_CAST")
fun bindRecyclerViewItems(view: RecyclerView, items: List<*>) {
    (view.adapter as? WidgetAdapter)?.setItems(items as List<Any>)
}