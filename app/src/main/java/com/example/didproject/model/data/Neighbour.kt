package com.example.didproject.model.data

import android.net.Uri

data class Neighbour (
    val id: String = "",
    val nickname: String = "",
    val name:String ="",
    var bio: String = "",
    val plants: ArrayList<UserPlant> = arrayListOf(),
    var imageUri: String = "",
    ){
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "id" to id,
                "nickname" to nickname,
                "name" to name,
                "bio" to bio,
                "plants" to plants,
                "imageUri" to imageUri,
            )
        }
    fun toUser(): User{
        return User(id,nickname,name,bio,plants,imageUri)
    }
    }


