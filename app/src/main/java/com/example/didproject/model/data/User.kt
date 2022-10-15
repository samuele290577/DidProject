package com.example.didproject.model.data

data class User(
    val id: String = "",
    var name: String = "",
    var nickname: String = "",
    var bio: String = "",
    val plants: HashMap<String,UserPlant> = hashMapOf(),
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
            "arduino" to arduino
        )
    }
    fun toNeighbour(): Neighbour{
        return Neighbour(id,nickname,name,bio,plants)
    }
}