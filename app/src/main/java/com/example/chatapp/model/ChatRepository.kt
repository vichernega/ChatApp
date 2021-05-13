package com.example.chatapp.model

import android.net.Uri
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
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class ChatRepository {

    private var _messagesListLiveData: MutableLiveData<MutableList<MessageItem>> = MutableLiveData()
    val messagesListLiveData get() = _messagesListLiveData


    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun send(message: String, imageUri: Uri?){

        //saving data to message object
        val authorName = UserObject.firstName + " " + UserObject.lastName
        val time = LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM))
        var image = ""
        imageUri?.let {
            image = saveImage(it)
        }

        MessageObject.set(UserObject.id, authorName, time.toString(), message, image)

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
        val messagesList = mutableListOf<MessageItem>()
        Firebase.firestore.collection(COLLECTION_USERS).document(UserObject.id)
            .collection(COLLECTION_CHATS).document(ChatObject.id)
            .collection(COLLECTION_MESSAGES)
            .get()
            .addOnSuccessListener {         // chat list as snapshot
                for (snapshot in it){
                    val mes = snapshot.toObject(Message::class.java)        // convert snapshot to local class
                    if (mes.authorId == UserObject.id) {                    // check whose message
                        messagesList.add(MessageItem(mes.authorId, mes.authorName, mes.time, mes.text, mes.image, TYPE_MY_MESSAGE))
                    } else {
                        messagesList.add(MessageItem(mes.authorId, mes.authorName, mes.time, mes.text, mes.image, TYPE_SENDER_MESSAGE))
                    }
                }
                _messagesListLiveData.postValue(messagesList)           // change liveData
                Log.d("MESSAGE", "Repository getChat() is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("MESSAGE", "Repository getChat() is FAILED: ${it.message}") }

    }

    // observe DB changes in current chat
    init {
        Firebase.firestore.collection(COLLECTION_USERS).document(UserObject.id)
            .collection(COLLECTION_CHATS).document(ChatObject.id)
            .collection(COLLECTION_MESSAGES)
            .addSnapshotListener { snapshot, error ->
                if (error != null){
                    Log.d("MESSAGE", "Repository observeChanges() is FAILED: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    getChat()
                    Log.d("MESSAGE", "Repository observeChanges() snapshot: ")
                } else { Log.d("MESSAGE", "Repository observeChanges() is FAILED: current data: null ${error?.message}") }
            }
    }

    suspend fun saveImage(uri: Uri): String{
        val imageName = UUID.randomUUID().toString()            // generating name for picture
        Firebase.storage.reference.child(FirebaseObject.FOLDER_IMAGES).child(UserObject.id).child(imageName)
            .putFile(uri)
            .addOnSuccessListener {
                Log.d("IMAGE", "Successful upload")
            }
            .addOnFailureListener{ Log.d("IMAGE", "Image upload is failed!!!")}
            .await()
        return imageName
    }

    fun clearList(){
        _messagesListLiveData.postValue(mutableListOf())
    }
}