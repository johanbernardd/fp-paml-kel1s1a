package com.example.contactappkel1.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.contactappkel1.model.AuthResult
import com.example.contactappkel1.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<AuthResult?>(null)
    val authState: StateFlow<AuthResult?> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            _authState.value = repository.login(email, password)
        }
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        profileImageUri: Uri? = null
    ) {
        viewModelScope.launch {
            _authState.value = AuthResult.Loading
            val fullName = "$firstName $lastName"

            if (profileImageUri != null) {
                repository.uploadProfileImage(profileImageUri) { imageUrl ->
                    viewModelScope.launch {
                        _authState.value = repository.register(email, password, fullName, imageUrl)
                    }
                }
            } else {
                _authState.value = repository.register(email, password, fullName, null)
            }
        }
    }



    fun clearAuthState() {
        _authState.value = null
    }


//    fun logout() = repository.logout()
//
//    fun checkLogin(): Boolean = repository.isLoggedIn()
}