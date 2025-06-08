package com.example.contactappkel1.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.contactappkel1.ui.theme.auth.LoginScreen
import com.example.contactappkel1.ui.theme.auth.RegisterScreen
import com.example.contactappkel1.ui.theme.auth.SplashScreen
import com.example.contactappkel1.ui.theme.contact.AddEditContactScreen
import com.example.contactappkel1.viewModel.AuthViewModel
import com.example.contactappkel1.ui.theme.auth.RegisterSuccessScreen
import com.example.contactappkel1.ui.theme.home.ContactListScreen
import com.example.contactappkel1.ui.theme.profile.ProfileScreen
import com.example.contactappkel1.viewModel.ContactViewModel

@Composable
fun AppNavGraph(navController: NavHostController, authViewModel: AuthViewModel, contactViewModel: ContactViewModel) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { SplashScreen(navController = navController) }
        composable("login") { LoginScreen(viewModel = authViewModel, navController = navController) }
        composable("register") { RegisterScreen(viewModel = authViewModel, navController = navController) }
        composable(
            "registerSuccess?firstName={firstName}&lastName={lastName}&photoUri={photoUri}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType; defaultValue = "" },
                navArgument("lastName") { type = NavType.StringType; defaultValue = "" },
                navArgument("photoUri") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
            val photoUri = backStackEntry.arguments?.getString("photoUri")

            RegisterSuccessScreen(
                navController = navController,
                firstName = firstName,
                lastName = lastName,
                photoUriString = photoUri
            )
        }
        composable("contact") { ContactListScreen(navController) }
        composable("addEdit") {
            AddEditContactScreen(navController = navController, contactId = null, contactViewModel = ContactViewModel())
        }
        composable(
            "addEdit/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            AddEditContactScreen(navController = navController, contactId = contactId, contactViewModel = ContactViewModel())
        }
        composable(
            "profile/{firstName}/{lastName}/{photoUrl}",
            arguments = listOf(
                navArgument("firstName") { type = NavType.StringType },
                navArgument("lastName") { type = NavType.StringType },
                navArgument("photoUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("firstName") ?: ""
            val lastName = backStackEntry.arguments?.getString("lastName") ?: ""
            val photoUrl = backStackEntry.arguments?.getString("photoUrl")?.takeIf { it.isNotBlank() }

            ProfileScreen(navController, firstName, lastName, photoUrl)
        }
    }
}
