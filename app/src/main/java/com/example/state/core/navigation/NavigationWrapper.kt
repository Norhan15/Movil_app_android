package com.example.state.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.state.login.presentation.LoginScreen
import com.example.state.login.presentation.LoginViewModel
import com.example.state.notes.presentation.NoteScreen
import com.example.state.notes.presentation.NoteViewModel
import com.example.state.register.presentation.RegisterScreen
import com.example.state.register.presentation.RegisterViewModel

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "Login") {

        composable("Login") {
            LoginScreen(LoginViewModel()) { destination ->
                navController.navigate(destination)
            }
        }
        composable("Register") {
            RegisterScreen(RegisterViewModel()) { destination ->
                navController.navigate(destination)
            }
        }

        composable("Notes/{userId}") { backStackEntry ->
            // Get userId from navigation arguments
            val userId = backStackEntry.arguments?.getString("userId")?.toIntOrNull() ?: -1

            // Instanciamos TextToSpeach con el context de Compose
            val context = androidx.compose.ui.platform.LocalContext.current
            val tts = androidx.compose.runtime.remember {
                com.example.state.core.hardware.TextToSpeach(context)
            }
            // Creamos el NoteViewModel pasándole el tts y userId como parámetros
            val noteViewModel = androidx.compose.runtime.remember {
                NoteViewModel(tts, userId)
            }
            // Ahora sí, llamamos a NoteScreen con este ViewModel
            NoteScreen(noteViewModel) { destination ->
                navController.navigate(destination)
            }
        }
    }
}
