package com.example.didproject.model.data

import android.net.Uri

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    var imageUri : Uri = Uri.parse(""),
    val plants: ArrayList<UserPlant> = arrayListOf<UserPlant>()
)