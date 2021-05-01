package com.example.chatapp.`object`

object User {

    var id : String = ""
    var firstName: String = ""
    var lastName: String = ""
    var phone: String = ""
    var email: String = ""

    fun setUser(id: String, firstName: String, lastName: String, phone: String, email: String){
        this.id = id
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
        this.email = email
    }
}

