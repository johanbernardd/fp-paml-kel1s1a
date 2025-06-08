package com.example.contactappkel1.ui.theme.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.contactappkel1.viewModel.AuthViewModel
import com.example.contactappkel1.model.AuthResult

@Composable
fun LoginScreen(viewModel: AuthViewModel, navController: NavController) {
    val authState by viewModel.authState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }


    Box(modifier = Modifier.fillMaxSize().padding(top = 150.dp), contentAlignment = Alignment.TopCenter) {
        Text("Contact App Kel1-S1A📱", style = MaterialTheme.typography.headlineMedium)
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Email Address")
            TextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("cth:jo@student.ub.ac.id") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Password")
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("cth:12345678") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { viewModel.login(email, password) }) {
            Text("Login")
        }

        TextButton(onClick = { navController.navigate("register") }) {
            Text("Don't have an account? Register")
        }

        when (authState) {
            is AuthResult.Loading -> CircularProgressIndicator()
            is AuthResult.Success -> {
                LaunchedEffect(authState) {
                    navController.navigate("contact") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            }
            is AuthResult.Error -> Text((authState as AuthResult.Error).message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}