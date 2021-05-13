package com.example.chatapp.`object`

data class MessageItem(
    val authorId: String,
    val authorName: String,
    val time: String,
    val text: String,
    val image: String,
    val messageType: Int
)