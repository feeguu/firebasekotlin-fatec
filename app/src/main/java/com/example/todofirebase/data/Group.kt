package com.example.todofirebase.data

data class Group(var id: String, var name: String, var tasks: MutableList<Task>) {
}