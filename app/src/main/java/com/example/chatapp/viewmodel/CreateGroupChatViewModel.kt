package com.example.chatapp.viewmodel

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.`object`.User
import com.example.chatapp.model.CreateGroupChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateGroupChatViewModel: ViewModel() {

    private val repo = CreateGroupChatRepository()
    private var _registerUserListLiveData = repo.registerUserListLiveData
    val registerUserListLiveData get() = _registerUserListLiveData

    var groupList: MutableList<User> = mutableListOf()
    private var _groupListLiveData: MutableLiveData<MutableList<User>> = MutableLiveData()
    val groupListLiveData get() = _groupListLiveData

    private var _isChatCreatingSuccessfulLiveData = repo.isChatCreatingSuccessfulLiveData
    val isChatCreatingSuccessfulLiveData get() = _isChatCreatingSuccessfulLiveData

    @RequiresApi(Build.VERSION_CODES.O)
    fun getUsers(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getUsers()
        }
    }

    fun receiveCheckboxInfo(position: Int, isChecked: Boolean){
        val contactList = _registerUserListLiveData.value

        if (isChecked){
            // add user from contact list to group list if checkbox is checked
            contactList?.get(position)?.let { groupList.add(it) }
        } else {
            // delete user if it is unchecked and contained in groupList
            if (groupList.contains(contactList!![position])){
                groupList.remove(contactList[position])
            }
        }
        _groupListLiveData.value = groupList
        Log.d("USERLIST", "groupList: $groupList")
    }

    fun createGroup(name: String){
        viewModelScope.launch(Dispatchers.IO){
            repo.createGroup(name, _groupListLiveData.value!!)
        }
    }
}