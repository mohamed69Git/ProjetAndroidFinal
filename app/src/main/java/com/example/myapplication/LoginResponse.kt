package com.example.myapplication

data class LoginResponse(
    val user: User? = null,
    val status: String? = null,
    val token: String? = null,
    val message: String? = null,
//    val account: Compte? = null
)
