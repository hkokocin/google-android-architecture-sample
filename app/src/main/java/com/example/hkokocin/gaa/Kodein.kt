package com.example.hkokocin.gaa

import android.app.Activity
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import com.example.hkokocin.gaa.adapter.WidgetAdapter
import com.example.hkokocin.gaa.data.GitHubRepository
import com.example.hkokocin.gaa.data.GitHubService
import com.example.hkokocin.gaa.users.GitHubActivity
import com.example.hkokocin.gaa.users.GitHubView
import com.example.hkokocin.gaa.users.UserSearchViewModel
import com.example.hkokocin.gaa.users.UserWidget
import com.github.salomonbrys.kodein.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


private var applicationScope = Kodein {}

fun initApplicationScope() {
    applicationScope = Kodein {

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        bind<GitHubService>() with singleton { retrofit.create(GitHubService::class.java) }
        bind<GitHubRepository>() with singleton { GitHubRepository(instance()) }
    }
}

fun baseActivityScope(activity: Activity) = Kodein {
    extend(applicationScope)

    bind<Context>() with singleton { activity }
    bind<Activity>() with singleton { activity }
    bind<LayoutInflater>() with singleton { activity.layoutInflater }

    bind<InputMethodManager>() with singleton { activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager }

    bind<WidgetAdapter>() with provider { WidgetAdapter(instance()) }
}

fun gitHubActivityScope(activity: GitHubActivity) = Kodein {
    extend(baseActivityScope(activity))

    bind<UserWidget>() with provider { UserWidget(instance()) }
    bind<Lifecycle>() with singleton { activity.lifecycle }
    bind<GitHubView>() with provider { GitHubView(instance(), instance(), provider(), instance()) }

    bind<UserSearchViewModel>() with singleton {
        ViewModelProviders
                .of(activity, ViewModelFactory(kodein))
                .get(UserSearchViewModel::class.java)
    }
}

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val injector: Kodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when (modelClass) {
        UserSearchViewModel::class.java -> UserSearchViewModel(injector.instance())
        else                            -> throw IllegalAccessException("unable to create ${modelClass.name}")
    } as T
}
