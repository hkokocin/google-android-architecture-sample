package com.example.hkokocin.gaa.users

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.data.GitHubRepository
import com.example.hkokocin.gaa.data.GitHubUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

data class UserSearchViewState(
        val users: List<GitHubUser> = emptyList(),
        val showProgress: Boolean = false,
        val errorResourceId: Int = 0
)

class UserSearchViewModel(
        private val repository: GitHubRepository
) : ViewModel() {

    val state = MutableLiveData<UserSearchViewState>()

//    init {
//        state.value = UserSearchViewState()
//    }

    private val disposables = CompositeDisposable()

    fun searchUsers(search: String) {
        repository.searchUser(search)
            .map { (items) -> UserSearchViewState(items) }
            .subscribeOn(Schedulers.io())
            .startWith(state.value.progress())
            .onErrorReturn { state.value.error() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { state.value = it }

//        repository.searchUser(search)
//            .map { (items) -> UserSearchViewState(items) }
//            .subscribeOn(Schedulers.io())
//            .startWith(state.value!!.copy(showProgress = true))
//            .onErrorReturn { state.value!!.copy(errorResourceId = R.string.search_failed) }
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { state.value = it }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

fun UserSearchViewState?.progress() = this?.copy(showProgress = true) ?: UserSearchViewState(showProgress = false)
fun UserSearchViewState?.error() = UserSearchViewState(this?.users ?: emptyList(), false, R.string.search_failed)

// liveData.postValue() instead of observeOn()