package com.example.todofirebase.ui.pages

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun Register(onRegister: () -> Unit, onLoginPressed: () -> Unit) {
    val emptyFieldToast =
        Toast.makeText(LocalContext.current, "Preencha todos os campos", Toast.LENGTH_SHORT)

    val wrongCredentialsToast =
        Toast.makeText(LocalContext.current, "Algo deu errado", Toast.LENGTH_LONG)

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    fun handleLogin() {
        email = email.trim()
        name = name.trim()
        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            emptyFieldToast.show()
            return
        }
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener { result ->
            val user = result.user
            val profileUpdates = userProfileChangeRequest {
                displayName = name
            }
            user?.updateProfile(profileUpdates)?.addOnSuccessListener {
                onRegister()
            }?.addOnFailureListener {
                Log.i("REGISTER", "Falha ao atualizar perfil: ${it.message}")
            }

            onRegister()


        }.addOnFailureListener { error ->
            if(error is FirebaseAuthException) {
                when (error.errorCode) {
                    "ERROR_INVALID_EMAIL" -> {
                        wrongCredentialsToast.setText("E-mail inválido")
                    }
                    "ERROR_WEAK_PASSWORD" -> {
                        wrongCredentialsToast.setText("Senha fraca")
                    }
                    "ERROR_EMAIL_ALREADY_IN_USE" -> {
                        wrongCredentialsToast.setText("E-mail já cadastrado")
                    }
                    else -> {
                        wrongCredentialsToast.setText("Algo deu errado")
                    }
                }
            }

            wrongCredentialsToast.show()

        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("nome") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.size(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("e-mail") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        showKeyboardOnFocus = true
                    )
                )
                Spacer(modifier = Modifier.size(16.dp))
                TextField(

                    value = password,
                    onValueChange = { password = it },
                    label = { Text("senha") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        showKeyboardOnFocus = true
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            passwordVisibility = !passwordVisibility
                        }) {
                            Icon(
                                imageVector = if (passwordVisibility)
                                    Icons.Outlined.VisibilityOff
                                else
                                    Icons.Outlined.Visibility,
                                contentDescription = "Password visibility"
                            )
                        }
                    },
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation()
                )
            }

            Column(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextButton(onClick = onLoginPressed) {
                    Text("já possui conta? entre aqui", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.size(8.dp))
                Button(onClick = { handleLogin() }) {
                    Text("entrar")
                }

            }
        }
    }
}