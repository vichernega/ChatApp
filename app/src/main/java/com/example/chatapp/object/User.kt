package com.example.chatapp.`object`

data class User(
    val id : String,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var image: String
){
    constructor(): this("", "", "", "", "", "")
}
