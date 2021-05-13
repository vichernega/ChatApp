package com.example.chatapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val repo = ChatRepository()
    private var _messagesListLiveData = repo.messagesListLiveData
    val messagesListLiveData get() = _messagesListLiveData

    fun sendText(message: String){
        viewModelScope.launch(Dispatchers.IO){
            repo.send(message, null)
        }
    }

    fun sendImage(image: Uri){
        viewModelScope.launch(Dispatchers.IO){
            repo.send("", image)
        }
    }

    fun getChat(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getChat()
        }
    }

    fun clearList(){
        repo.clearList()
    }

}