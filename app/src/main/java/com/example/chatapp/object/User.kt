package com.example.chatapp.`object`

import com.google.firebase.firestore.DocumentSnapshot

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

    fun setUser(firstName: String, lastName: String, phone: String){
        this.firstName = firstName
        this.lastName = lastName
        this.phone = phone
    }

    fun setUser(documentSnapshot: DocumentSnapshot){
        val user = documentSnapshot.toObject(User::class.java)
        if (user != null){
            id = user.id
            firstName = user.firstName
            lastName = user.lastName
            phone = user.phone
            email = user.email
        }
    }

    fun clear(){
        id = ""
        firstName = ""
        lastName = ""
        phone = ""
        email = ""
    }

}

