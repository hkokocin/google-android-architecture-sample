package com.example.hkokocin.gaa.users

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.hkokocin.gaa.data.GitHubRepository
import com.example.hkokocin.gaa.data.GitHubUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class UserSearchViewModel(
        private val repository: GitHubRepository
) : ViewModel() {

    val users = MutableLiveData<List<GitHubUser>>()
    val showProgress = MutableLiveData<Boolean>()

    private val disposables = CompositeDisposable()

    fun searchUsers(search: String) {
        showProgress.value = true
        repository.searchUser(search)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doAfterTerminate { showProgress.value = false }
            .subscribe { (items) -> users.value = items }
    }

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}

// liveData.postValue() instead of observeOn()