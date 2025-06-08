package com.example.contactappkel1.repository

import android.net.Uri
import com.example.contactappkel1.model.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.UUID

class AuthRepository(private val firebaseAuth: FirebaseAuth) {

    suspend fun login(email: String, password: String): AuthResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(result.user?.uid ?: "")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Login failed")
        }
    }

    suspend fun register(email: String, password: String, displayName: String, photoUri: String?): AuthResult {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            // Update profil Firebase Auth
            user?.updateProfile(
                userProfileChangeRequest {
                    this.displayName = displayName
                    if (!photoUri.isNullOrEmpty()) {
                        this.photoUri = Uri.parse(photoUri)
                    }
                }
            )?.await()

            AuthResult.Success(user?.uid ?: "")
        } catch (e: Exception) {
            AuthResult.Error(e.message ?: "Register failed")
        }
    }


    fun uploadProfileImage(uri: Uri, onResult: (String?) -> Unit) {
        val storageRef = Firebase.storage.reference
        val filename = "profile_images/${UUID.randomUUID()}.jpg"
        val imageRef = storageRef.child(filename)

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onResult(downloadUri.toString())
                }.addOnFailureListener {
                    onResult(null)
                }
            }
            .addOnFailureListener {
                onResult(null)
            }
    }


//    fun logout() = firebaseAuth.signOut()
//
//    fun isLoggedIn(): Boolean = firebaseAuth.currentUser != null
}