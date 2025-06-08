package com.example.contactappkel1.ui.theme.contact

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.contactappkel1.model.Contact
import com.example.contactappkel1.viewModel.ContactViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContactScreen(
    navController: NavController,
    contactViewModel: ContactViewModel,
    contactId: String?
) {
//    val context = LocalContext.current
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val scrollState = rememberScrollState()

    // Load contact for editing
    LaunchedEffect(contactId) {
        if (contactId != null) {
            contactViewModel.getContactById(contactId) { contact ->
                contact?.let {
                    firstName = it.firstName
                    lastName = it.lastName
                    phone = it.phone
                    email = it.email
                    company = it.company
                    imageUri = it.photoUrl?.let { uriString -> Uri.parse(uriString) }
                }
            }
        }
    }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                imageUri = uri
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (contactId == null) "Add Contact" else "Edit Contact") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // First Name
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("First Name")
                TextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("cth: Johanes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Last Name
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Last Name")
                TextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("cth: Bernard") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Phone
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Telephone")
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("cth: 081234567890") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Email
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Email Address")
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("cth: jo@student.ub.ac.id") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Company
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.Start) {
                Text("Perusahaan")
                TextField(
                    value = company,
                    onValueChange = { company = it },
                    label = { Text("cth: FILKOM UB") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Upload Foto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = { launcher.launch("image/*") }) {
                    Text("Pilih Foto Profil")
                }
            }

            imageUri?.let {
                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tombol Tambah atau Edit
            Button(
                onClick = {
                    val contact = Contact(
                        firstName = firstName,
                        lastName = lastName,
                        phone = phone,
                        email = email,
                        company = company
                    )

                    if (contactId == null) {
                        contactViewModel.addContact(contact, imageUri)
                    } else {
                        contactViewModel.updateContact(contactId, contact, imageUri)
                    }

                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (contactId == null) "Add Contact" else "Update Contact")
            }
        }
    }
}






