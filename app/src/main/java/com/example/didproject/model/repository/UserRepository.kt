package com.example.didproject.model.repository

import com.example.didproject.model.data.User
import com.google.firebase.database.DataSnapshot

class UserRepository (val db:DataSnapshot){

    fun getUserById(id:String): User {
        return db.child(id).getValue(User::class.java)!!
    }

}