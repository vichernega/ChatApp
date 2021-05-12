package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ChatsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatsViewModel: ViewModel() {

    private val repo = ChatsRepository()
    private var _chatsListLiveData = repo.chatsListLiveData
    val chatsListLiveData get() = _chatsListLiveData

    fun getChats(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getChats()
        }
    }
}