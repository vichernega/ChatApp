package com.example.chatapp.model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.UserObject
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ChangePersonalInfoRepository {

    private var _isSaved: MutableLiveData<Boolean> = MutableLiveData()
    val isSaved: MutableLiveData<Boolean> get() = _isSaved

    fun saveUserInfo(firstName: String, lastName: String, phone: String){
        UserObject.setUser(firstName, lastName, phone)
        Firebase.firestore.collection(FirebaseObject.COLLECTION_USERS).document(UserObject.id)
                .set(UserObject)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        Log.d("FIRESTORE", "saveUser() is SUCCESSFUL")
                        _isSaved.postValue(true)
                    }
                    else { Log.d("FIRESTORE", "saveUser() is FAILED: ${it.exception?.message}") }
                }
    }
}