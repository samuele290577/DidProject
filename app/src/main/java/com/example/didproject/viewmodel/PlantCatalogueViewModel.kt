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


class PlantCatalogueViewModel : ViewModel() {

    private val categories : Array<String> = arrayOf("Aromatiche", "Grasse","Bulbose", "Arbusti", "Da interno")
    private val dr = Firebase.database.reference
    private val storageRef: StorageReference = Firebase.storage.reference
    private val _plantList = MutableLiveData<ArrayList<Plant>>()
    private val _photoList = MutableLiveData<Map<String, Uri>>()
    val photoList: LiveData<Map<String, Uri>> = _photoList
    private val _categoryPhoto = MutableLiveData<Map<String, Uri>>()
    val categoryPhoto: LiveData<Map<String, Uri>> = _categoryPhoto

    init {
            readList()
    }

    fun getByInputName(name: String): List<Plant> {
        return _plantList.value?.filter {
            it.name.lowercase().contains(name.lowercase()) ||
                    it.scName.lowercase().contains(name.lowercase()) }!!
    }

    fun getByCategory(category: String): List<Plant> {
        return _plantList.value?.filter { it.category == category }?.toList()!!
    }

    fun getByName(name:String):Plant{
        return _plantList.value?.first { it.name==name }!!
    }

    private fun readList(){
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val plantListTmp = arrayListOf<Plant>()
                dataSnapshot.children.forEach { a->
                    val plantTmp=a.getValue(Plant::class.java)!!
                    plantListTmp.add(plantTmp)
                }
                downloadPlantPhoto(plantListTmp)
                downloadCategoryPhoto()
                _plantList.value=plantListTmp
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("plants").addListenerForSingleValueEvent(userEventListener)
    }

    private fun downloadPlantPhoto(list:List<Plant>) {
        val map : MutableMap<String,Uri> = mutableMapOf()
        list.forEach {
            val plantImagesRef: StorageReference = storageRef.child("catalogue/${it.name.lowercase()}")
            plantImagesRef.downloadUrl.addOnSuccessListener { res ->
                map[it.name]=res
                _photoList.value=map
            }.addOnFailureListener{ _ ->
                map[it.name]=Uri.parse("")
                _photoList.value=map
            }
        }
    }

    private fun downloadCategoryPhoto() {
        val map : MutableMap<String,Uri> = mutableMapOf()
        categories.forEach {
            val plantImagesRef: StorageReference = storageRef.child("catalogue/category/${it.lowercase()}")
            plantImagesRef.downloadUrl.addOnSuccessListener { res ->
                map[it]=res
                _categoryPhoto.value=map
            }.addOnFailureListener{ _ ->
                map[it]=Uri.parse("")
                _categoryPhoto.value=map
            }
        }
    }
}