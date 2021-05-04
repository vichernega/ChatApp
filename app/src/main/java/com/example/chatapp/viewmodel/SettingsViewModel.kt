package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.`object`.User
import com.example.chatapp.model.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel: ViewModel() {

    private val repo = SettingsRepository()
    private var _userLiveData: MutableLiveData<User> = repo.userLiveData
    val userLiveData: MutableLiveData<User> get() = _userLiveData

    fun getUserInfo(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.getUserInfo()
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.logout()
        }
    }
}