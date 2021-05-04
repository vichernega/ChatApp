package com.example.chatapp.model

import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.FirestoreObject
import com.example.chatapp.`object`.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SettingsRepository {

    private var _userLiveData: MutableLiveData<User> = MutableLiveData()
    val userLiveData: MutableLiveData<User> get() = _userLiveData

    fun getUserInfo(){
        FirestoreObject.getUser()
    }

    fun logout(){
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser == null){
            User.clear()
            _userLiveData.postValue(User)
        }
    }
}