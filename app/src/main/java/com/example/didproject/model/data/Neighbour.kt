package com.example.didproject.model.data

data class Neighbour (
    val id: String = "",
    val nickname: String = "",
    val name:String ="",
    var bio: String = "",
    val plants: HashMap<String,UserPlant> = hashMapOf(),
    ){
    fun toUser(): User{
        return User(id,nickname,name,bio,plants)
    }
    }


