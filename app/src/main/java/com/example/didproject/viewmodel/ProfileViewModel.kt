package com.example.didproject.viewmodel

import android.content.ContentValues
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata


class ProfileViewModel : ViewModel() {

    private val dr = Firebase.database.reference
    private val storageRef: StorageReference = Firebase.storage.reference

    private val _photo = MutableLiveData<Uri>()
    private val _user = MutableLiveData<User>()
    val photo: LiveData<Uri> = _photo
    val user: LiveData<User> = _user
    private val _trivia = MutableLiveData<List<String>>()
    val trivia: LiveData<List<String>> = _trivia
    private val _personalNeighbourPlantPhoto = MutableLiveData<HashMap<String,Uri>>()
    val personalNeighbourPlantPhoto: LiveData<HashMap<String,Uri>> = _personalNeighbourPlantPhoto
    private val _neighboursPhoto = MutableLiveData<Map<String,Uri>>()
    val neighboursPhoto: LiveData<Map<String,Uri>> = _neighboursPhoto
    private val _personalPlantPhoto = MutableLiveData<HashMap<String,Uri>>()
    val personalPlantPhoto: LiveData<HashMap<String,Uri>> = _personalPlantPhoto


    init {
        val id=FirebaseAuth.getInstance().currentUser?.email
        if(id!=null)
            readUser(id.replace(".",","))
    }

    fun updateProfile(user: User, state:Int){
        writeUser(user)
        when(state){
            0->downloadPhoto()
            1->downloadPersonalPlant()
            2->if(!_user.value?.friends.isNullOrEmpty())
                downloadFriendPhoto()
        }
    }

    private fun writeUser(user: User){
        dr.child("users").child(user.id).setValue(user)
    }

    private fun readUser(id: String){
        val user = User(FirebaseAuth.getInstance().currentUser?.email!!.replace(".",","))
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                _user.value = dataSnapshot.getValue(User::class.java)?:user
                if(_user.value?.name.isNullOrEmpty()){
                    _user.value?.name=FirebaseAuth.getInstance().currentUser?.displayName?:"name"
                    updateProfile(_user.value!!,0)
                }
                if(_user.value?.nickname.isNullOrEmpty()) {
                    _user.value?.nickname =
                        FirebaseAuth.getInstance().currentUser?.displayName ?: "nickname"
                    updateProfile(_user.value!!,0)
                }
                downloadPhoto()
                if(!_user.value?.friends.isNullOrEmpty())
                    downloadFriendPhoto()
                downloadPersonalPlant()
                downloadTrivia()
            // ...
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("users").child(id).addValueEventListener(userEventListener)
    }

    private fun downloadTrivia(){
        val triviaEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _trivia.value = dataSnapshot.getValue() as List<String>
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("trivia").addValueEventListener(triviaEventListener)
    }

    fun uploadPhoto(uri: Uri, userFlag:Boolean=true, additionalPath:String=""){
        val profileImagesRef: StorageReference = if(userFlag)
            storageRef.child("profile/${_user.value?.id}")
        else
            storageRef.child("profile/${_user.value?.id}/${additionalPath}")
        val metadata = storageMetadata {
            contentType = "image/jpeg"
        }
        profileImagesRef.putFile(uri, metadata).addOnSuccessListener{
            if (userFlag) downloadPhoto()
            else downloadPersonalPlant()
        }
    }

    private fun downloadFriendPhoto(){
        val map : MutableMap<String, Uri> = mutableMapOf()
        _user.value?.friends?.values?.forEach {
            val friendImagesRef: StorageReference = storageRef.child("profile/${it.id}")
            friendImagesRef.downloadUrl.addOnSuccessListener { uri->
                 map[it.id]=uri
                 if(map.size==_user.value?.friends?.size)
                     _neighboursPhoto.value=map
            }.addOnFailureListener { _ ->
                map[it.id]=Uri.parse("")
                if(map.size==_user.value?.friends?.size)
                    _neighboursPhoto.value=map
            }
        }
    }

    private fun downloadPhoto() {
        val profileImagesRef: StorageReference = storageRef.child("profile/${_user.value?.id}")
            profileImagesRef.downloadUrl.addOnSuccessListener {
                _photo.value = it
            }.addOnFailureListener{
                _photo.value=Uri.parse("")
            }
    }

    fun removePlantImage(key:String){
        val profileImagesRef: StorageReference = storageRef.child("profile/${_user.value?.id}/${key}")
        if(_personalPlantPhoto.value?.containsKey(key) == true)
        profileImagesRef.delete().addOnSuccessListener {
            _personalPlantPhoto.value?.remove(key)
        }
    }

    fun getAvailableArduinos() : Array<String> {
        return _user.value?.arduino?.filter { a->a.value.plantIndex<=0 }?.keys?.toTypedArray()?: arrayOf()
    }

    fun downloadPersonalPlant(){
        val plantPhotoList : HashMap<String,Uri> = hashMapOf()
        _user.value?.plants?.keys?.forEach { key ->
            val profileImagesRef: StorageReference = storageRef.child("profile/${_user.value?.id}/${key}")
            profileImagesRef.downloadUrl.addOnSuccessListener {
                plantPhotoList[key]=it
                if(plantPhotoList.size==_user.value?.plants?.size)
                    _personalPlantPhoto.value = plantPhotoList
            }.addOnFailureListener{
                standardPlantPhoto(plantPhotoList, key, _user.value?.plants!![key]?.plant?.name!!, _user.value?.plants?.size!!)
            }
        }
    }

    fun downloadPersonalPlantNeighbour(id: String){
        val neighbour = user.value?.friends!![id]
        val plantPhotoList : HashMap<String,Uri> = hashMapOf()
        neighbour?.plants?.keys?.forEach { key ->
            val profileImagesRef: StorageReference = storageRef.child("profile/${neighbour.id}/${key}")
            profileImagesRef.downloadUrl.addOnSuccessListener {
                plantPhotoList[key]=it
                if(plantPhotoList.size== neighbour.plants.size)
                    _personalNeighbourPlantPhoto.value = plantPhotoList
            }.addOnFailureListener{
                standardPlantPhoto(plantPhotoList, key, neighbour.plants[key]?.plant?.name!!, neighbour.plants.size,false)
            }
        }
    }

    private fun standardPlantPhoto(map:HashMap<String, Uri>, key:String, plantName:String, size:Int, personal:Boolean=true){
        val profileImagesRef: StorageReference = storageRef.child("catalogue/${plantName.lowercase()}")
        profileImagesRef.downloadUrl.addOnSuccessListener {
            map[key]=it
            if(map.size==size) {
                if (personal )
                    _personalPlantPhoto.value = map
                else
                    _personalNeighbourPlantPhoto.value = map
            }
        }.addOnFailureListener{
            map[key]=Uri.parse("")
            if(map.size==size){
                if (personal)
                    _personalPlantPhoto.value = map
                else
                    _personalNeighbourPlantPhoto.value = map
            }
        }
    }

}