package com.example.didproject.model.data

data class User(
    val id: String = "",
    var nickname: String = "",
    var bio: String = "",
    val plants: ArrayList<UserPlant> = arrayListOf<UserPlant>()
)