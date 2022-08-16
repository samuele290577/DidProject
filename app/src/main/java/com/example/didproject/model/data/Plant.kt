package com.example.didproject.model.data

data class Plant(
    val name: String="",
    val scName: String="",
    val photo: String="",
    val info: String="",
    val tips: String="",
    val care: String="",
    val category: String="",
    val difficulty: Int=0,
    val sun: Int=0,
    val humidity: Int=0
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "scName" to scName,
            "photo" to photo,
            "info" to info,
            "tips" to tips,
            "care" to care,
            "category" to category,
            "difficulty" to difficulty,
            "humidity" to humidity,
            "sun" to sun
        )
    }
}