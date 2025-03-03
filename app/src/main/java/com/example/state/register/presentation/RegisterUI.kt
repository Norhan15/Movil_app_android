package com.example.state.register.presentation


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import com.example.state.register.data.model.CreateUserRequest
import kotlinx.coroutines.launch
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.state.ui.theme.Beige
import com.example.state.ui.theme.GrayBlue
import com.example.state.ui.theme.LightGray
import com.example.state.ui.theme.Teal
import androidx.compose.runtime.LaunchedEffect

@Preview(showBackground = true)
@Composable
fun preview_RegisterScreen() {
    val fakeViewModel = RegisterViewModel()
    RegisterScreen(
        fakeViewModel,
        onNavigate = TODO()
    )
}

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel, onNavigate: (String) -> Unit) {
    val username: String by registerViewModel.username.observeAsState("")
    val password: String by registerViewModel.password.observeAsState("")
    val success: Boolean by registerViewModel.success.observeAsState(false)
    val error: String by registerViewModel.error.observeAsState("")
    var isPasswordVisible by remember { mutableStateOf(false) }
    val navigationCommand: String? by registerViewModel.navigationCommand.observeAsState(null)

    LaunchedEffect(navigationCommand) {
        if (navigationCommand != null) {
            onNavigate(navigationCommand!!)
            registerViewModel.onNavigationHandled()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Teal)
            .padding(16.dp),            // <--- Se agrega el padding de 16.dp
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Regístrate",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Beige,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp) // Separación inferior
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = username,
            onValueChange = { registerViewModel.onChangeUsername(it) },
            label = { Text("Nombre de Usuario") },  // en español para mayor consistencia
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Person Icon") },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { registerViewModel.onChangePassword(it) },
            label = { Text("Contraseña") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Lock Icon") },
            trailingIcon = {
                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                    Icon(
                        imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña"
                    )
                }
            },
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Si hay un mensaje de error, lo mostramos
        if (error.isNotEmpty()) {
            Text(
                text = error,
                color = GrayBlue, // o el color que prefieras para mostrar errores
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(
            onClick = {
                val user = CreateUserRequest(username, password)
                registerViewModel.viewModelScope.launch {
                    registerViewModel.onClick(user)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Beige,
                contentColor = Teal
            )
        ) {
            Text(
                text = "Sign up",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = buildAnnotatedString {
                append("¿Ya tienes una cuenta? ")
                pushStringAnnotation(tag = "LOGIN", annotation = "login")
                withStyle(
                    style = SpanStyle(
                        color = Beige,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Inicia Sesión")
                }
                pop()
            },
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    registerViewModel.navigateToLogin()
                }
        )
    }
}
