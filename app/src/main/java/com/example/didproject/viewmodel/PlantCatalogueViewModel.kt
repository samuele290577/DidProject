package com.example.didproject.viewmodel

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.Plant
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage


class PlantCatalogueViewModel( ) : ViewModel() {

    private val dr = Firebase.database.reference
    private val storageRef: StorageReference = Firebase.storage.reference
    private val _plantList = MutableLiveData<ArrayList<Plant>>()
    val plantList: LiveData<ArrayList<Plant>> = _plantList
    private val _photoList = MutableLiveData<Map<String, Uri>>()
    val photoList: LiveData<Map<String, Uri>> = _photoList

    init {
            readList()
    }

    fun getByInputName(name: String):List<Plant>{
        val list = plantList.value?.filter{ it.name.contains(name)}!!
        return list
    }

    fun getByCategory(category: String):List<Plant>{
        val list=plantList.value?.filter { it.category==category }?.toList()!!
        return list
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
                    val plantTmp=a.getValue(Plant::class.java)!!
                    plantListTmp.add(plantTmp);
                }
                downloadPlantPhoto(plantListTmp)
                _plantList.value=plantListTmp
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("plants").addListenerForSingleValueEvent(userEventListener)
    }

    private fun downloadPlantPhoto(list:List<Plant>) {
        val map : MutableMap<String,Uri> = mutableMapOf()
        list.forEach {
            val plantImagesRef: StorageReference = storageRef.child("catalogue/${it.name}")
            plantImagesRef.downloadUrl.addOnSuccessListener { res ->
                map[it.name]=res
                _photoList.value=map
            }
        }
    }
}