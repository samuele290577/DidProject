package com.example.didproject.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.didproject.model.data.User


class ProfileViewModel( ) : ViewModel() {

    private val _profile = MutableLiveData<User>()
    val profile: LiveData<User> = _profile

    fun updateProfile(user: User){
        _profile.value=user
    }

}