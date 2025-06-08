package com.example.contactappkel1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.contactappkel1.navigation.AppNavGraph
import com.example.contactappkel1.repository.AuthRepository
import com.example.contactappkel1.viewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.contactappkel1.viewModel.ContactViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val firebaseAuth = FirebaseAuth.getInstance()
            val authRepo = AuthRepository(firebaseAuth)
            val authViewModel = AuthViewModel(authRepo)

            AppNavGraph(navController = navController, authViewModel = authViewModel, contactViewModel = ContactViewModel())
        }
    }
}
