package com.example.todofirebase.data

import java.util.Date

data class Task(var id: String, var name: String, var description: String, var completed: Boolean, var createdAt: Date, var updatedAt: Date) {
}
