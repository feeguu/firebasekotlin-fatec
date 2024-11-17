package com.example.todofirebase.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExpandLess
import androidx.compose.material.icons.outlined.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todofirebase.data.Group
import com.example.todofirebase.data.Task
import java.util.Date

@Composable
fun GroupCard(group: Group, onTaskCheck: (Group, Task, Boolean) -> Unit, onPressCreateTask: () -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    Card(onClick = { expanded = !expanded }, modifier = Modifier.fillMaxWidth(0.95f)) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = group.name)
                Icon(
                    imageVector = if (expanded) Icons.Outlined.ExpandLess else Icons.Outlined.ExpandMore,
                    contentDescription = if(expanded) "Expand less" else "Expand more"
                )
            }
            Spacer(modifier = Modifier.size(8.dp))
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically(),
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    group.tasks.forEach { task ->
                        TaskCard(task = task, onCheck = {
                            onTaskCheck(group, task, it)
                        })
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                    TextButton(onClick = onPressCreateTask, modifier = Modifier.align(Alignment.End)) {
                        Text(text = "adicionar tarefa")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun GroupCardPreview() {
    GroupCard(
        onTaskCheck = { _, _, _ -> },
        group = Group(
            id = "",
            name = "Group 1",
            tasks = mutableListOf(
                Task(
                    id = "",
                    name = "Task 1",
                    completed = false,
                    description = "",
                    createdAt = Date(),
                    updatedAt = Date()
                ),
            )
        )
    )
}