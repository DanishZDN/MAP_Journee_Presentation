package com.android.example.mobileapp_journeeg9.model

import java.time.LocalDate
import java.time.LocalDateTime
import com.google.android.gms.maps.model.LatLng

data class JournalEntry(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: LocalDateTime = LocalDateTime.now(),
    val imageUrl: String? = null,
    val location: LatLng? = null,
    val locationName: String? = null,
    val userId: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDateString(): String {
        return "${date.hour}:${String.format("%02d", date.minute)} ${if (date.hour >= 12) "pm" else "am"}"
    }

    fun toLocalDate(): LocalDate = date.toLocalDate()
}

data class UserProfile(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val address: String = "",
    val dateOfBirth: String = "",
    val profilePictureUrl: String? = null
)
