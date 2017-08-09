package com.example.hkokocin.gaa.data

import com.google.gson.annotations.SerializedName

data class UserSearchResult(val items: List<GitHubUser>)

data class GitHubUser(
        @field:SerializedName("login") val name: String = "",
        @field:SerializedName("avatar_url") val avatarUrl: String = "",
        @field:SerializedName("html_url") val url: String = ""
)