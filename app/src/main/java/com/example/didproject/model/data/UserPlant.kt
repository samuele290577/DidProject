package com.example.didproject.model.data

data class UserPlant(
    val plant: Plant = Plant(),
    var nickname: String = "",
    var customPhoto: String = "",
    var location: String = "",
    var status: String = "",
)