package com.example.chatapp.`object`

data class User(
    var id : String,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var image: String
){
    constructor(): this("", "", "", "", "", "")
    constructor(user: UserObject): this(user.id, user.firstName, user.lastName, user.phone, user.email, user.image)
}
