package com.example.didproject.model.repository

import com.example.didproject.model.data.User
import com.example.didproject.model.data.UserPlant
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class UserPlantRepository() {

    private val dr = Firebase.database.reference

    fun writeUser(userPlant: UserPlant, user: User){
        dr.child("users").child(user.id).setValue(user);
    }
}