package com.example.didproject.model.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList

class PlantRepository () {
    private val dr = Firebase.database.reference
    private var plantList = arrayListOf<Plant>()


        fun getAll(){

        }
        fun findByCategory(category:String):List<Plant>{

            return plantList.filter { a -> a.category==category }
        }


}