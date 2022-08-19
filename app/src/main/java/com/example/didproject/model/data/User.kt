package com.example.didproject.model.data

import android.net.Uri

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    var imageUri : String = "",
    val plants: ArrayList<UserPlant> = arrayListOf(),
    val friends: ArrayList<User> = arrayListOf()
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "nickname" to nickname,
            "bio" to bio,
            "imageUri" to imageUri,
            "plants" to plants,
            "friends" to friends
        )
    }
}