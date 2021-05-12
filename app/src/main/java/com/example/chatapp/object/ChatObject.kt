package com.example.chatapp.`object`

object ChatObject {
    var id = ""
    var name = ""
    var members = listOf<User>()

    fun create(id: String, name: String, members: List<User>){
        this.id = id
        this.name = name
        this.members = members
    }

    fun set(chat: Chat){
        this.id = chat.id
        this.name = chat.name
        this.members = chat.members
    }
}