package com.example.chatapp.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.`object`.User
import com.example.chatapp.model.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/** Shared ViewModel for SettingsFragment and ChooseImageActionDialogFragment*/
class SettingsViewModel: ViewModel() {

    private val repo = SettingsRepository()
    private var _userLiveData: MutableLiveData<User> = repo.userLiveData            //User live data
    val userLiveData: MutableLiveData<User> get() = _userLiveData

    private var _isProfileImageDownloadedLiveData = repo.isProfileImageDownloadedLiveData   // flag for user photo download
    val isProfileImageDownloadedLiveData get() = _isProfileImageDownloadedLiveData

    // get user photo if it exists
    fun getProfileImage(){
        if (User.image.isNotEmpty()){
            viewModelScope.launch(Dispatchers.IO){
                repo.getProfileImage()
            }
        }
    }

    // save chosen image
    fun saveUserImage(uri: Uri){
        viewModelScope.launch(Dispatchers.IO){
            repo.saveUserPhoto(uri)         // save photo to DB
            repo.getProfileImage()          // get saved photo from DB to show in fragment
        }
    }

    fun logout(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.logout()
        }
    }

}