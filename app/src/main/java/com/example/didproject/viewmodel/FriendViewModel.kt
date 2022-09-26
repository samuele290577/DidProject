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


class FriendViewModel : ViewModel() {


    private val dr = Firebase.database.reference
    private val storageRef: StorageReference = Firebase.storage.reference
    private val _users = MutableLiveData<ArrayList<User>>()
    private val userId = FirebaseAuth.getInstance().currentUser?.email?.replace(".",",")
    val users: LiveData<ArrayList<User>> = _users

    init {
        readAllUsers()
    }

    fun searchUser(name: String): List<User>{
        val userList=_users.value?.filter { it.name.lowercase().contains(name.lowercase()) || it.nickname.lowercase().contains(name.lowercase()) } as List<User>
        downloadPhoto(userList)
        return userList
    }

    fun searchUserById(id: String):User{
        return _users.value?.first{it.id==id}!!
    }

    private fun readAllUsers(){
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userListTmp = arrayListOf<User>()
                dataSnapshot.children.forEach {
                    val userTmp=it.getValue(User::class.java)!!
                    if(userTmp.id!= userId)
                        userListTmp.add(userTmp)
                }
                _users.value=userListTmp
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("users").addValueEventListener(userEventListener)
    }

    private fun downloadPhoto(list:List<User>)   {
        list.forEach {
            val profileImagesRef: StorageReference = storageRef.child("profile/${it.id}")
            profileImagesRef.downloadUrl.addOnSuccessListener { res ->
                it.imageUri=res.toString()
            }.addOnFailureListener { _ ->
                it.imageUri = ""
            }
        }
    }

}