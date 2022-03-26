package com.example.didproject.model.data

data class Plant(
    val id: String,
    val name: String,
    val scName: String,
    val info: String,
    val tips: String,
    val care: String,
    val type: String,
    val hashMap: HashMap<Any, Any>
){
    constructor():this("","","","","","","", hashMapOf<Any,Any>())
}