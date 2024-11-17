package com.example.todofirebase.ui.pages

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todofirebase.data.Group
import com.example.todofirebase.data.Task
import com.example.todofirebase.ui.components.GroupCard
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

@Composable
fun Home(
    onLogout: () -> Unit,
    onPressCreateGroup: () -> Unit,
    onPressCreateTask: (String) -> Unit
) {
    val auth = FirebaseAuth.getInstance()

    val db = FirebaseFirestore.getInstance()
    val user = auth.currentUser
    var groups = remember {
        mutableStateMapOf<String, Group>()
    }


    fun handleLogout() {
        auth.signOut()
        onLogout()
    }

    fun fetchGroups() {
        user?.let {
            db.collection("users/${it.uid}/groups")
                .get()
                .addOnSuccessListener { result ->
                    for (document in result) {
                        val tasks = mutableListOf<Task>()
                        db.collection("users/${it.uid}/groups/${document.id}/tasks")
                            .get()
                            .addOnSuccessListener { result ->
                                for (task in result) {
                                    tasks.add(
                                        Task(
                                            id = task.id,
                                            name = task.getString("name") ?: "",
                                            description = task.getString("description") ?: "",
                                            createdAt = task.getDate("createdAt") ?: Date(),
                                            updatedAt = task.getDate("updatedAt") ?: Date(),
                                            completed = task.getBoolean("completed") ?: false,
                                        )
                                    )
                                }
                            }
                        groups[document.id] = Group(
                            id = document.id,
                            name = document.data["name"] as String,
                            tasks = tasks
                        )
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("Home", "Error getting documents.", exception)
                }
        }
    }


    fun handleCheckTask(group: Group, task: Task, value: Boolean) {
        db.collection("users/${user?.uid}/groups/${group.id}/tasks")
            .document(task.id)
            .update("completed", value)
            .addOnSuccessListener {
                Log.d("Home", "DocumentSnapshot successfully updated!")
                val updatedTask = task.copy(completed = value)
                val updatedTasks = group.tasks.map {
                    if (it.id == task.id) updatedTask else it
                }
                groups[group.id] = group.copy(tasks = updatedTasks.toMutableList())

            }
            .addOnFailureListener { e ->
                Log.w("Home", "Error updating document", e)
            }
    }

    LaunchedEffect(Unit) {
        fetchGroups()
    }


    Log.d("Home", "Groups: ${groups}")

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onPressCreateGroup,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = "Add group");
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Text(text = "Logado como: ${user?.displayName}")
            Text(text = "E-mail: ${user?.email}")

            Button(onClick = { handleLogout() }) {
                Text(text = "Sair")
            }

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(groups.entries.toList()) { group ->
                    GroupCard(group = group.value, onTaskCheck = { group, task, value ->
                        handleCheckTask(group, task, value)
                    }, onPressCreateTask = {
                        onPressCreateTask(group.value.id)
                    })
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    }
}