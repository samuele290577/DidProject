package com.example.didproject.model.data

data class User(
    val id: String = "a",
    var name: String = "a",
    var nickname: String = "a",
    var bio: String = "a",
    val plants: ArrayList<UserPlant> = arrayListOf<UserPlant>()
)