package com.example.didproject.viewmodel

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.User
import com.example.didproject.model.data.UserPlant
import com.example.didproject.model.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ProfileViewModel( ) : ViewModel() {

    private val dr = Firebase.database.reference
    private val ur = UserRepository()
    private val _user = MutableLiveData<User>()
    private lateinit var listener :ValueEventListener
    val user: LiveData<User> = _user

    init {
        val id=FirebaseAuth.getInstance().currentUser?.email
        if(id!=null)
            readUser(id.replace(".",","))
    }

    fun updateProfile(user: User){
        ur.writeUser(user)
    }

    private fun readUser(id: String){
        var user = User(FirebaseAuth.getInstance().currentUser?.email!!.replace(".",","))
        val userEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                _user.value = dataSnapshot.getValue(User::class.java)?:user
                // ...
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(ContentValues.TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        dr.child("users").child(id).addValueEventListener(userEventListener)
    }

    fun removePlant (pos : Int){
        _user.value?.plants?.removeAt(pos)
    }

}