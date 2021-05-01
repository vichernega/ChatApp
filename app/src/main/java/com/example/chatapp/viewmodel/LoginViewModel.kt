package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.RegisterRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val repo = RegisterRepository()
    private var _userLiveData: MutableLiveData<FirebaseUser> = repo.userLiveData
    val userLiveData: MutableLiveData<FirebaseUser> get() = _userLiveData

    fun signUp(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.login(email, password)
        }
    }
}