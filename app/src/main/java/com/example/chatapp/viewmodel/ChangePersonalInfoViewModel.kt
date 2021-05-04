package com.example.chatapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.model.ChangePersonalInfoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePersonalInfoViewModel: ViewModel() {

    private val repo = ChangePersonalInfoRepository()
    private var _isSaved = repo.isSaved
    val isSaved: MutableLiveData<Boolean> get() = _isSaved

    fun saveUserInfo(firstName: String, lastName: String, phone: String){
        viewModelScope.launch(Dispatchers.IO) {
            repo.saveUserInfo(firstName, lastName, phone)
        }
    }
}