package com.example.contactappkel1.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.example.contactappkel1.model.Contact
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class ContactRepository {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    fun listenToContacts(userId: String, onDataChange: (List<Contact>) -> Unit) {
        db.collection("contacts")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ContactRepo", "Listen failed", error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val contacts = snapshot.documents.mapNotNull {
                        it.toObject(Contact::class.java)?.copy(id = it.id)
                    }
                    onDataChange(contacts)
                }
            }
    }

//    suspend fun getContacts(userId: String): List<Contact> {
//        val snapshot = db.collection("contacts")
//            .whereEqualTo("userId", userId)
//            .get()
//            .await()
//
//        return snapshot.documents.mapNotNull { it.toObject(Contact::class.java)?.copy(id = it.id) }
//    }

    suspend fun addContact(userId: String, contact: Contact, imageUri: Uri?) {
        val newDocRef = db.collection("contacts").document()
        val contactWithId = contact.copy(id = newDocRef.id, userId = userId)

        if (imageUri != null) {
            val storageRef = storage.reference.child("contact_images/${newDocRef.id}.jpg")
            storageRef.putFile(imageUri).await()
            val downloadUrl = storageRef.downloadUrl.await()
            val contactWithPhoto = contactWithId.copy(photoUrl = downloadUrl.toString())
            newDocRef.set(contactWithPhoto).await()
        } else {
            newDocRef.set(contactWithId).await()
        }
    }

    suspend fun updateContact(userId: String, contactId: String, contact: Contact, imageUri: Uri?) {
        val contactRef = db.collection("contacts").document(contactId)

        if (imageUri != null) {
            // Simpan foto ke path yang sama dengan addContact
            val photoRef = storage.reference.child("contact_images/$contactId.jpg")
            photoRef.putFile(imageUri).await()
            val downloadUrl = photoRef.downloadUrl.await()

            // Copy semua field lengkap
            val updatedContact = contact.copy(
                photoUrl = downloadUrl.toString(),
                userId = userId,
                id = contactId
            )
            contactRef.set(updatedContact, SetOptions.merge()).await()
        } else {
            // Kalau tidak ada foto baru, ambil dulu photoUrl lama dari firestore
            val snapshot = contactRef.get().await()
            val oldPhotoUrl = snapshot.getString("photoUrl")

            val updatedContact = contact.copy(
                photoUrl = oldPhotoUrl,
                userId = userId,
                id = contactId
            )
            contactRef.set(updatedContact, SetOptions.merge()).await()
        }
    }

    suspend fun deleteContact(userId: String, contactId: String) {
        db.collection("contacts").document(contactId).delete().await()
    }

    fun getContactById(userId: String, contactId: String, onResult: (Contact?) -> Unit) {
        db.collection("contacts").document(contactId).get()
            .addOnSuccessListener { document ->
                val contact = document.toObject(Contact::class.java)
                onResult(contact)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }
}


