package com.example.contactappkel1.model

data class Contact(
    val id: String = "",
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val email: String = "",
    val company: String = "",
    val photoUrl: String? = null
) {
    val fullName: String
        get() = "$firstName $lastName".trim()
}
