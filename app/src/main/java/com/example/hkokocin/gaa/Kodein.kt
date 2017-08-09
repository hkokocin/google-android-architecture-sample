package com.example.hkokocin.gaa

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import com.example.hkokocin.gaa.adapter.WidgetAdapter
import com.example.hkokocin.gaa.data.GitHubRepository
import com.example.hkokocin.gaa.data.GitHubService
import com.example.hkokocin.gaa.users.GitHubActivity
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

    bind<WidgetAdapter>() with provider { WidgetAdapter(instance()) }
}

fun gitHubActivityScope(activity: GitHubActivity) = Kodein {
    extend(baseActivityScope(activity))

    bind<UserSearchViewModel>() with provider { UserSearchViewModel(instance()) }
    bind<UserWidget>() with provider { UserWidget(instance()) }
}

