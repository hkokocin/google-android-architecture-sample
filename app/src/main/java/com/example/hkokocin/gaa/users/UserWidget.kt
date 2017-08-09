package com.example.hkokocin.gaa.users

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.hkokocin.gaa.R
import com.example.hkokocin.gaa.adapter.Widget
import com.example.hkokocin.gaa.data.GitHubUser

class UserWidget(
        val activity: Activity
) : Widget<GitHubUser>(R.layout.user_item) {

    private val ivAvatar: ImageView by viewId(R.id.iv_avatar)
    private val tvName: TextView by viewId(R.id.tv_name)
    private val clContainer: ConstraintLayout by viewId(R.id.cl_container)

    override fun setData(data: GitHubUser) {
        Glide.with(ivAvatar).load(data.avatarUrl).into(ivAvatar)
        tvName.text = data.name
        clContainer.setOnClickListener {
            activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(data.url)))
        }
    }
}