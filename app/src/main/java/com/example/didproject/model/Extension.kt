package com.example.didproject.model

import com.example.didproject.R
import com.example.didproject.model.`object`.PlantCategory

fun String.getImageResourceId() : Int {
    return when(this){
        PlantCategory.ARBUSTI -> R.mipmap.arbusti
        PlantCategory.AROMATICHE -> R.mipmap.aromatiche
        PlantCategory.BULBOSE -> R.mipmap.bulbose
        PlantCategory.GRASSE -> R.mipmap.grasse
        PlantCategory.INTERNO -> R.mipmap.da_interno
        else -> R.mipmap.arbusti
    }
}