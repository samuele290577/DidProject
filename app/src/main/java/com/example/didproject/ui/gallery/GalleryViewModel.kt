package com.example.didproject.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GalleryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Samuele Modifica per vedere se funge commit, anche Matteo per lo stesso motivo, prova"
    }
    val text: LiveData<String> = _text
}