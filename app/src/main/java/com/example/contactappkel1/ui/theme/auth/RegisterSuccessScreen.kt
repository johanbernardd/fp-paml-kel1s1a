package com.example.contactappkel1.ui.theme.auth

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter

@Composable
fun RegisterSuccessScreen(
    navController: NavHostController,
    firstName: String,
    lastName: String,
    photoUriString: String?
) {
    val photoUri = photoUriString?.takeIf { it.isNotEmpty() && it != "null" }?.let { Uri.parse(it) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Foto Profil
        photoUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Nama Lengkap
        if (firstName.isNotBlank() || lastName.isNotBlank()) {
            Text(
                text = "Welcome, $firstName $lastName",
                style = MaterialTheme.typography.headlineSmall,
            )
        } else {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineSmall,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Account created successfully!",
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 24.sp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("contact") {
                    popUpTo("registerSuccess") { inclusive = true }
                }
            }
        ) {
            Text(text = "Contact List")
        }
    }
}