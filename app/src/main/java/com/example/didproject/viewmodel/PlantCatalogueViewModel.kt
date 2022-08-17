package com.example.didproject.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.Plant
import com.example.didproject.model.data.User
import com.example.didproject.model.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class PlantCatalogueViewModel( ) : ViewModel() {

    private val dr = Firebase.database.reference
    private lateinit var p :String
    private val _plantList = MutableLiveData<ArrayList<Plant>>()
    val plantList: LiveData<ArrayList<Plant>> = _plantList
    private val _plantNamesList = MutableLiveData<ArrayList<String>>()
    val plantNamesList: LiveData<ArrayList<String>> = _plantNamesList

    init {
            readList()
    }

    fun getByInputName(name: String):List<Plant>{
        return plantList.value?.filter{ it.name.contains(name)}!!
    }

    fun getByCategory(category: String):List<Plant>{
        return plantList.value?.filter { it.category==category }?.toList()!!
    }

    fun getByName(name:String):Plant{
        return plantList.value?.first { it.name==name }!!
    }

    private fun readList(){
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val plantListTmp = arrayListOf<Plant>()
                val plantNamesListTmp = arrayListOf<String>()
                // Get Post object and use the values to update the UI
                dataSnapshot.children.forEach { a->
                    plantListTmp.add(a.getValue(Plant::class.java)!!);
                    plantNamesListTmp.add(a.key!!)
                }
                _plantList.value=plantListTmp
                _plantNamesList.value=plantNamesListTmp
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("plants").addListenerForSingleValueEvent(userEventListener)
    }

}