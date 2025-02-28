package com.example.projectapp.model

data class Transaction(
    val id: Long = 0,
    val date: String,
    val title: String,
    val type: String,
    val amount: String,
    val tax: String
)
