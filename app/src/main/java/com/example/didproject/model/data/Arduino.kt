package com.example.didproject.model.data

data class Arduino (
    var active:Int=0,
    var plantIndex:Int=0,
){
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "active" to active,
            "plantIndex" to plantIndex
        )
    }
}