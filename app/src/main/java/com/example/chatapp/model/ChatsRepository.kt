package com.example.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.Chat
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_CHATS
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_USERS
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChatsRepository {

    private var _chatsListLiveData: MutableLiveData<MutableList<Chat>> = MutableLiveData()
    val chatsListLiveData get() = _chatsListLiveData

    fun getChats(){
        var list = mutableListOf<Chat>()
        Firebase.firestore.collection(COLLECTION_USERS).document(Firebase.auth.currentUser.uid)
            .collection(COLLECTION_CHATS)
            .get()
            .addOnSuccessListener {
                for (chat in it){
                    list.add(chat.toObject(Chat::class.java))   // convert every documentSnapshot to Chat data class
                }
                _chatsListLiveData.postValue(list)      // add list to  live data
                Log.d("CHATS", "REPO: getChats() is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("CHATS", "REPO: getChats() is FAILED") }
    }
}