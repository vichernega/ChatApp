package com.example.chatapp.`object`

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

object FirebaseObject {

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference

    const val COLLECTION_USERS = "users"
    const val FOLDER_IMAGES = "images/"


    /** FIRESTORE FUNCTIONS */

    /**Saving user information in database*/
    fun saveUser(user: User){
        firestore.collection(COLLECTION_USERS).document(user.id)
                .set(user)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("FIRESTORE", "saveUser() is successful")
                    }
                    else { Log.d("FIRESTORE", "saveUser() is FAILED: ${it.exception?.message}") }
                }
    }

    /**Get user information from database*/
    fun getUser(){
        firestore.collection(COLLECTION_USERS).document(Firebase.auth.currentUser.uid)
                .get()
                .addOnSuccessListener {
                    User.setUser(it)
                    Log.d("FIRESTORE", "getUser() is SUCCESSFUL")
                }
                .addOnFailureListener { Log.d("FIRESTORE", "getUser() is FAILED: ${it.message}") }
    }

    /** Get user image from DB*/
    suspend fun getUserProfileImage(user: User): Boolean{
        var isSuccess = false
        val imageRef = storage.child(FOLDER_IMAGES + user.id + "/" + user.image)

        imageRef.downloadUrl
            .addOnSuccessListener {
                ImageObject.uri = it       // save downloaded uri to imageObject
                isSuccess = true
                Log.d("IMAGE", "getUserProfileImage1() is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("IMAGE", "getUserProfileImage1() is FAILED: ${it.message}") }
            .await()
        return isSuccess        // true if task is successful
    }
}