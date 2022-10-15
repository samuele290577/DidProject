package com.example.didproject.model.data

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    //TODO: setup another way to save photos in usere, userplant
    val plants: HashMap<String,UserPlant> = hashMapOf(),
    var imageUri : String = "",
    val friends: MutableMap<String, Neighbour> = mutableMapOf(),
    val arduino: Map<String, Arduino> = mapOf(),
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "nickname" to nickname,
            "bio" to bio,
            "plants" to plants,
            "friends" to friends,
            "imageUri" to imageUri,
            "arduino" to arduino
        )
    }
    fun toNeighbour(): Neighbour{
        return Neighbour(id,nickname,name,bio,plants)
    }
}