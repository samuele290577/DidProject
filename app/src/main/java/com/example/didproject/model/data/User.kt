package com.example.didproject.model.data

import android.net.Uri

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    var imageUri : String = "",
    //TODO: setup another way to save photos in usere, userplant
    val plants: ArrayList<UserPlant> = arrayListOf(),
    val friends: ArrayList<User> = arrayListOf(),
    //TODO: is it convenient to save friends as user or it will be better as list of string?
    val arduino: Map<String, Arduino> = mapOf(),
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "nickname" to nickname,
            "bio" to bio,
            "imageUri" to imageUri,
            "plants" to plants,
            "friends" to friends,
            "arduino" to arduino
        )
    }
}