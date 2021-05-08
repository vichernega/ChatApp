package com.example.chatapp.model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.User
import com.example.chatapp.utilits.showToast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterRepository {

    private var _userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLiveData: MutableLiveData<FirebaseUser> get() = _userLiveData

    fun signUp(firstName: String, lastName: String, phone: String, email: String, password: String){
        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _userLiveData.postValue(Firebase.auth.currentUser)                                 // change liveData
                    User.setUser(Firebase.auth.uid.toString(), firstName, lastName, phone, email)   // save data in local user
                    FirebaseObject.saveUser(User)                                                   // save data in remote user
                    showToast("Welcome!")
                }
                else{
                    showToast(it.exception?.message.toString())
                    Log.d("AUTHFIREBASE", it.exception.toString())
                }
            }
    }


    fun login(email: String, password: String){
        Firebase.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    _userLiveData.postValue(Firebase.auth.currentUser)
                    showToast("Welcome!")
                }
                else{
                    showToast(it.exception?.message.toString())
                    Log.d("AUTHFIREBASE", it.exception.toString())
                }
            }
    }
}