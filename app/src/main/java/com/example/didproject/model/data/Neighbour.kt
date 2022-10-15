package com.example.didproject.model.data

data class Neighbour (
    val id: String = "",
    val nickname: String = "",
    val name:String ="",
    var bio: String = "",
    val plants: HashMap<String,UserPlant> = hashMapOf(),
    ){
        fun toMap(): Map<String, Any?> {
            return mapOf(
                "id" to id,
                "nickname" to nickname,
                "name" to name,
                "bio" to bio,
                "plants" to plants,
            )
        }
    fun toUser(): User{
        return User(id,nickname,name,bio,plants)
    }
    }


