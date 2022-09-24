package com.example.didproject.model.data

data class UserPlant(
    val plantName: String = "",
    var date: Long = 0,
    var nickname: String = "",
    var customPhoto: String = "",
    var location: String = "",
    var status: Int = 100,
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "plantName" to plantName,
            "date" to date,
            "nickname" to nickname,
            "customPhoto" to customPhoto,
            "location" to location,
            "status" to status
        )
    }
}