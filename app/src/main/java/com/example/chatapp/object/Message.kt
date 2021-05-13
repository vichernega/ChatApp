package com.example.chatapp.`object`

data class Message(
    val authorId: String,
    val authorName: String,
    val time: String,
    val text: String,
    val image: String
){
    constructor() : this ("","", "", "", "")
}