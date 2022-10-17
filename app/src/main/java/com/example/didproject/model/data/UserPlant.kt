package com.example.didproject.model.data

data class UserPlant(
    val plantName: String = "",
    var date: Long = 0,
    var nickname: String = "",
    var plant : Plant = Plant(),
    var location: String = "",
    var status: Int = 100,
)