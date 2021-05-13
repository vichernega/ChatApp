package com.example.chatapp.`object`

import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.chatapp.utilits.APP_ACTIVITY
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

object FirebaseObject {

    private val firestore = Firebase.firestore
    private val storage = Firebase.storage.reference

    const val COLLECTION_USERS = "users"
    const val COLLECTION_CHATS = "chats"
    const val COLLECTION_MESSAGES = "messages"
    const val FOLDER_IMAGES = "images/"


    /** FIRESTORE FUNCTIONS */

    /**Saving user information in database*/
    fun saveUser(user: UserObject){
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
                    UserObject.setUser(it)
                    Log.d("FIRESTORE", "getUser() is SUCCESSFUL")
                }
                .addOnFailureListener { Log.d("FIRESTORE", "getUser() is FAILED: ${it.message}") }
    }

    /** Get user image from DB*/
    suspend fun getUserProfileImage(user: UserObject): Boolean{
        var isSuccess = false
        val imageRef = storage.child(FOLDER_IMAGES + user.id + "/" + user.image)

        imageRef.downloadUrl
            .addOnSuccessListener {
                ImageObject.uri = it       // save downloaded uri to imageObject
                isSuccess = true
                Log.d("IMAGE", "getUserProfileImage() is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("IMAGE", "getUserProfileImage() is FAILED: ${it.message}") }
            .await()
        return isSuccess        // true if task is successful
    }

    suspend fun createChat(chat: ChatObject): Boolean{
        var isSuccessful = false

        // create chat in every user in DB
        for (member in chat.members){
            firestore.collection(COLLECTION_USERS).document(member.id)
                .collection(COLLECTION_CHATS).document(chat.id)
                .set(chat)
                .addOnSuccessListener {
                    isSuccessful = true
                    Log.d("CHAT", "createChat() is SUCCESSFUL")
                }
                .addOnFailureListener { Log.d("CHAT", "createChat() is FAILED: ${it.message}") }
                .await()
        }
        return isSuccessful
    }

    fun downloadImage(uid: String, image: String, imageView: ImageView){
        val imageRef = Firebase.storage.reference
            .child(FOLDER_IMAGES + uid + "/" + image)

        imageRef.downloadUrl                            // get image uri
            .addOnSuccessListener {
                Glide.with(APP_ACTIVITY)                // load image from DB and show in ImageView
                    .load(it)
                    .into(imageView)
                Log.d("IMAGE", "downloadImage() is SUCCESSFUL")
            }
            .addOnFailureListener {
                Log.d("IMAGE", "downloadImage() is FAILED: ${it.message}")
            }
    }

}