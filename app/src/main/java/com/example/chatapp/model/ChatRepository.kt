package com.example.chatapp.model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.*
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_CHATS
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_MESSAGES
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_USERS
import com.example.chatapp.utilits.TYPE_MY_MESSAGE
import com.example.chatapp.utilits.TYPE_SENDER_MESSAGE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class ChatRepository {

    private var _messagesListLiveData: MutableLiveData<MutableList<MessageItem>> = MutableLiveData()
    val messagesListLiveData get() = _messagesListLiveData


    @RequiresApi(Build.VERSION_CODES.O)
    fun send(message: String){

        //saving data to message object
        val authorName = UserObject.firstName + " " + UserObject.lastName
        val time = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT))

        MessageObject.set(UserObject.id, authorName, time.toString(), message)

        // saving message in db
        for (member in ChatObject.members){
            Firebase.firestore.collection(COLLECTION_USERS).document(member.id)
                .collection(COLLECTION_CHATS).document(ChatObject.id)
                .collection(COLLECTION_MESSAGES).document(time.toString())
                .set(MessageObject)
                .addOnSuccessListener { Log.d("MESSAGE", "Repository send() is SUCCESSFUL") }
                .addOnFailureListener { Log.d("MESSAGE", "Repository send() is FAILED") }
        }

    }

    fun getChat(){
        var messagesList = mutableListOf<MessageItem>()
        Firebase.firestore.collection(COLLECTION_USERS).document(UserObject.id)
            .collection(COLLECTION_CHATS).document(ChatObject.id)
            .collection(COLLECTION_MESSAGES)
            .get()
            .addOnSuccessListener {         // chat list as snapshot
                for (snapshot in it){
                    val mes = snapshot.toObject(Message::class.java)        // convert snapshot to local class
                    if (mes.authorId == UserObject.id) {                    // check whose message
                        messagesList.add(MessageItem(mes.authorName, mes.text, mes.time, TYPE_MY_MESSAGE))
                    } else {
                        messagesList.add(MessageItem(mes.authorName, mes.text, mes.time, TYPE_SENDER_MESSAGE))
                    }
                }
                _messagesListLiveData.postValue(messagesList)           // change liveData
                Log.d("MESSAGE", "Repository getChat() is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("MESSAGE", "Repository getChat() is FAILED: ${it.message}") }
    }

}