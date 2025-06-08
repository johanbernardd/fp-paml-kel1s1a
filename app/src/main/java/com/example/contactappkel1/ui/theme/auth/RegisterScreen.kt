package com.example.contactappkel1.ui.theme.auth

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.contactappkel1.viewModel.AuthViewModel
import com.example.contactappkel1.model.AuthResult
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image


@Composable
fun RegisterScreen(viewModel: AuthViewModel, navController: NavController) {
    val authState by viewModel.authState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    var hasNavigated by remember { mutableStateOf(false) }

//    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 60.dp, start = 24.dp),
        contentAlignment = Alignment.TopStart
    ) {
        Text(
            "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("First Name")
            TextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("cth:Johanes") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
            Text("Last Name")
            TextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("cth:Bernard") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
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
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(modifier = Modifier.padding(start = 8.dp), onClick = { launcher.launch("image/*") }) {
                Text("Pilih Foto Profil")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))

        // Gambar profil
        profileImageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Profile Image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

//        Button(onClick = { launcher.launch("image/*") }, horizontalAlignment = Alignment.CenterHorizontally ,modifier = Modifier.padding(start = 30.dp)) {
//            Text("Pilih Foto Profil")
//        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            viewModel.register(
                email = email,
                password = password,
                firstName = firstName,
                lastName = lastName,
                profileImageUri = profileImageUri
            )
        }) {
            Text("Register")
        }


        TextButton(onClick = { navController.popBackStack() }) {
            Text("Already have an account? Login")
        }

        when (authState) {
            is AuthResult.Loading -> CircularProgressIndicator()
            is AuthResult.Success -> {
                if (!hasNavigated) {
                    hasNavigated = true
                    val encodedUri = Uri.encode(profileImageUri?.toString() ?: "")
                    navController.navigate("registerSuccess?firstName=${firstName}&lastName=${lastName}&photoUri=${encodedUri}") {
                        popUpTo("register") { inclusive = true }
                    }
                    viewModel.clearAuthState()
                }
            }
            is AuthResult.Error -> Text((authState as AuthResult.Error).message, color = MaterialTheme.colorScheme.error)
            else -> {}
        }
    }
}
