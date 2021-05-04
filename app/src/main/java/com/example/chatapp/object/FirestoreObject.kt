package com.example.chatapp.`object`

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirestoreObject {

    private val firestore = Firebase.firestore

    const val COLLECTION_USERS = "users"


    /**Saving user information in database*/
    fun saveUser(user: User){
        firestore.collection(COLLECTION_USERS).document(user.id)
                .set(user)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("FIRESTORE", "saveUser() is successful")
                    }
                    else { Log.d("FIRESTORE", "!!! ERROR in saveUser()") }
                }
    }

    /**Get user information from database*/
    fun getUser(){
        firestore.collection(COLLECTION_USERS).document(Firebase.auth.currentUser.uid)
                .get()
                .addOnSuccessListener {
                    User.setUser(it)
                    Log.d("FIRESTORE", "getUser() is successful")
                }
                .addOnFailureListener { Log.d("FIRESTORE", "!!! ERROR in getUser()") }
    }
}