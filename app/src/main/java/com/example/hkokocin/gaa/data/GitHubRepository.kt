package com.example.hkokocin.gaa.data


class GitHubRepository(
        private val service: GitHubService
) {
    fun searchUser(search: String) = service.searchUsers(search)
}