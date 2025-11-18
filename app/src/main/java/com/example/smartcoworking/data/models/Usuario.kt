package com.example.smartcoworking.data.models

data class Usuario(
    val id: String,
    val nome: String,
    val email: String,
    val senhaHash: String
)
