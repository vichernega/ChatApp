package com.example.chatapp.model

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.User
import com.example.chatapp.other.SingleLiveEvent
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

class SettingsRepository {

    private var _userLiveData = SingleLiveEvent<User>()             // MutableLiveData (can have only 1 observer)
    val userLiveData: SingleLiveEvent<User> get() = _userLiveData

    // live data to check if photo is successfully downloaded from DB
    private var _isProfileImageDownloadedLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val isProfileImageDownloadedLiveData get() = _isProfileImageDownloadedLiveData


    fun logout(){
        Firebase.auth.signOut()
        if (Firebase.auth.currentUser == null){
            User.clear()
            _userLiveData.postValue(User)
        }
    }

    suspend fun getProfileImage(){
        // attempt to download photo and save result in liveData
        _isProfileImageDownloadedLiveData.postValue(FirebaseObject.getUserProfileImage(User))
    }

    suspend fun saveUserPhoto(uri: Uri){
        val imageName = UUID.randomUUID().toString()            // generating name for picture
        Firebase.storage.reference.child(FirebaseObject.FOLDER_IMAGES).child(User.id).child(imageName)
            .putFile(uri)
            .addOnSuccessListener {
                User.image = imageName                          // if task is successful save ref on image to user
                FirebaseObject.saveUser(User)                   // updating user data in firestore (with photo)
                Log.d("IMAGE", "Successful upload")
            }
            .addOnFailureListener{ Log.d("IMAGE", "Image upload is failed!!!")}
            .await()
    }
}