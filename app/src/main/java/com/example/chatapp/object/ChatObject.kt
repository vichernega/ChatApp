package com.example.chatapp.`object`

object ChatObject {
    var id = ""
    var name = ""
    var members = listOf<User>()
    var messages = mutableListOf<String>()

    fun create(id: String, name: String, members: List<User>){
        this.id = id
        this.name = name
        this.members = members
    }
}