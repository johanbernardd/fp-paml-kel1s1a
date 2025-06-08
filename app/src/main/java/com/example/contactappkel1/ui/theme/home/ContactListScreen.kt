package com.example.contactappkel1.ui.theme.home

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contactappkel1.viewModel.ContactViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(
    navController: NavController,
    contactViewModel: ContactViewModel = viewModel()
) {
    val contacts by contactViewModel.contacts.collectAsState()

    val user = FirebaseAuth.getInstance().currentUser
    val displayName = user?.displayName ?: "User"
    val photoUrl = user?.photoUrl?.toString()

    val (firstName, lastName) = displayName.split(" ").let {
        val first = it.getOrNull(0) ?: ""
        val last = it.drop(1).joinToString(" ")
        first to last
    }

    LaunchedEffect(Unit) {
        contactViewModel.listenToContacts()
    }

    DisposableEffect(navController) {
        val callback = NavController.OnDestinationChangedListener { _, _, _ ->
            contactViewModel.listenToContacts()
        }
        navController.addOnDestinationChangedListener(callback)
        onDispose {
            navController.removeOnDestinationChangedListener(callback)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Contact") },
                actions = {
                    TextButton(onClick = {
                        navController.navigate("profile/${Uri.encode(firstName)}/${Uri.encode(lastName)}/${Uri.encode(photoUrl ?: "")}")
                    }) {
                        Text("My Profile", color = Color.Black)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("addEdit")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Contact")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (contacts.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No contacts available.")
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(contacts) { contact ->
                        ContactItem(
                            contact = contact,
                            onEdit = {
                                navController.navigate("addEdit/${contact.id}")
                            },
                            onDelete = {
                                contactViewModel.deleteContact(contact.id)
                            }
                        )
                    }
                }
            }
        }
    }
}




