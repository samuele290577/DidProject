package com.example.didproject.viewmodel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FriendViewModel : ViewModel() {

    private val dr = Firebase.database.reference
    private val _users = MutableLiveData<ArrayList<User>>()
    val users: LiveData<ArrayList<User>> = _users

    init {
        readAllUsers()
    }

    fun searchUser(name: String): List<User>{
        return _users.value?.filter { it.name.contains(name) || it.nickname.contains(name) } as List<User>
    }

    fun searchUserById(id: String):User{
        return _users.value?.first{it.id==id}!!
    }

    private fun readAllUsers(){
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userListTmp = arrayListOf<User>()
                dataSnapshot.children.forEach { userListTmp.add(it.getValue(User::class.java)!!) }
                _users.value=userListTmp
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("users").addValueEventListener(userEventListener)
    }

}