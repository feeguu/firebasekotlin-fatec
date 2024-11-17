package com.example.todofirebase.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.todofirebase.data.Task
import java.util.Date

@Composable
fun TaskCard(task: Task, onCheck: (Boolean) -> Unit = {}) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Checkbox(checked = task.completed, onCheckedChange = {onCheck(it)})

        Text(text = task.name)
    }
}

@Preview(showBackground = true)
@Composable
fun TaskCardPreview() {
    TaskCard(
        task = Task(
            id = "",
            name = "Task 1",
            completed = false,
            description = "",
            createdAt = Date(),
            updatedAt = Date()
        )
    )
}