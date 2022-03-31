package com.example.didproject.model.data

data class Plant(
    val id: String="",
    val name: String="",
    val scName: String="",
    val photo: String="",
    val info: String="",
    val tips: String="",
    val care: String="",
    val type: String="",
    val options: HashMap<Any, Any> = hashMapOf<Any, Any>()
)