package com.example.hkokocin.gaa.data

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface GitHubService {

    @GET("search/users")
    fun searchUsers(@Query("q") search: String): Single<UserSearchResult>
}