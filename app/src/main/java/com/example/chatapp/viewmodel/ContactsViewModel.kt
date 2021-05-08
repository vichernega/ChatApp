package com.example.chatapp.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ContactsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactsViewModel: ViewModel() {

    private val repo = ContactsRepository()
    private var _registerUserList = repo.registerUserList
    val registerUserList get() = _registerUserList

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getUsers()
        }
    }
}