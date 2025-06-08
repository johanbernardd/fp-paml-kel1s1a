package com.example.contactappkel1.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.example.contactappkel1.model.Contact
import com.example.contactappkel1.repository.ContactRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ContactViewModel : ViewModel() {
    private val repository = ContactRepository()
    private val auth = FirebaseAuth.getInstance()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts

    fun listenToContacts() {
        val userId = auth.currentUser?.uid ?: return
        repository.listenToContacts(userId) { contactList ->
            _contacts.value = contactList
        }
    }

    fun addContact(contact: Contact, imageUri: Uri?) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.addContact(userId, contact, imageUri)
        }
    }

    fun updateContact(contactId: String, contact: Contact, imageUri: Uri?) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.updateContact(userId, contactId, contact, imageUri)
        }
    }

    fun deleteContact(contactId: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            repository.deleteContact(userId, contactId)
        }
    }

    fun getContactById(contactId: String, onResult: (Contact?) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        repository.getContactById(userId, contactId, onResult)
    }
}


