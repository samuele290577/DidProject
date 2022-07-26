package com.example.didproject.model.repository

import android.content.ContentValues.TAG
import android.location.GnssAntennaInfo
import android.util.Log
import com.example.didproject.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


// ...

class UserRepository (){


// ...
    private val dr = Firebase.database.reference



    fun writeUser(user: User){
        dr.child("users").child(user.id).setValue(user);
    }

}