package com.example.chatapp.`object`

data class Chat(
    val id: String,
    val name: String,
    val members: List<User>,
){
    constructor() : this("", "", listOf())
}
