package com.example.todofirebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import com.example.todofirebase.ui.pages.CreateGroup
import com.example.todofirebase.ui.pages.CreateTask
import com.example.todofirebase.ui.pages.Home
import com.example.todofirebase.ui.pages.Login
import com.example.todofirebase.ui.pages.Register
import com.example.todofirebase.ui.theme.TodoFirebaseTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.initialize
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        val firebaseApp = Firebase.initialize(this)

        auth = FirebaseAuth.getInstance(firebaseApp!!)
        db = FirebaseFirestore.getInstance(firebaseApp)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App(auth)
        }
    }
}


@Serializable
object HomeScreen

@Serializable
object LoginScreen

@Serializable
object RegisterScreen

@Serializable
data class CreateTaskDialog(val groupId: String)

@Serializable
object CreateGroupDialog

@Composable
fun App(auth: FirebaseAuth) {
    val navController = rememberNavController()
    val startDestination = auth.currentUser?.let { HomeScreen } ?: LoginScreen
    TodoFirebaseTheme(dynamicColor = false) {
        NavHost(navController = navController, startDestination = startDestination) {
            composable<LoginScreen> {
                Login(onLogin = {
                    navController.navigate(HomeScreen)
                }, onRegisterPressed = {
                    navController.navigate(RegisterScreen)
                })
            }
            composable<RegisterScreen> {
             Register(onRegister = {
                 navController.navigate(HomeScreen)
             }, onLoginPressed = {
                 navController.navigate(LoginScreen)
             })
            }
            composable<HomeScreen> {
                Home(onLogout = {
                    navController.navigate(LoginScreen)
                }, onPressCreateTask = { groupId ->
                    navController.navigate(CreateTaskDialog(groupId))
                }, onPressCreateGroup = {
                    navController.navigate(CreateGroupDialog)
                })
            }

            dialog<CreateTaskDialog> {
                val groupId = it.arguments?.getString("groupId") ?: ""
                CreateTask(groupId = groupId, onBack = {
                    navController.navigate(HomeScreen)

                })
            }

            dialog<CreateGroupDialog> {
                CreateGroup(onBack = {
                    navController.navigate(HomeScreen)
                })
            }
        }
    }
}
