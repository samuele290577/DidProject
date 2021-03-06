package com.example.didproject.model.data

import android.net.Uri

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    var imageUri : String = "",
    val plants: List<UserPlant> = emptyList<UserPlant>()
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "nickname" to nickname,
            "bio" to bio,
            "imageUri" to imageUri,
            "plants" to plants
        )
    }
}