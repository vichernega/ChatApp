package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel: ViewModel() {

    private val repo = ChatRepository()
    private var _messagesListLiveData = repo.messagesListLiveData
    val messagesListLiveData get() = _messagesListLiveData

    fun send(message: String){
        viewModelScope.launch(Dispatchers.IO){
            repo.send(message)
        }
    }

    fun getChat(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getChat()
        }
    }

    fun observeChanges(){
        viewModelScope.launch(Dispatchers.IO){
            //repo.observeChanges()
        }
    }
}