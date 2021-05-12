package com.example.chatapp.model

import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.ChatObject
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.User
import com.example.chatapp.`object`.UserObject
import com.example.chatapp.utilits.APP_ACTIVITY
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*

class CreateGroupChatRepository {

    private var _registerUsersListLiveData: MutableLiveData<List<User>> = MutableLiveData()
    val registerUserListLiveData get() = _registerUsersListLiveData

    private var _isChatCreatingSuccessfulLiveData = MutableLiveData<Boolean>()
    val isChatCreatingSuccessfulLiveData get() = _isChatCreatingSuccessfulLiveData

    suspend fun createGroup(name: String, members: MutableList<User>){
        val chatId = UUID.randomUUID().toString()               // generate chat id
        ChatObject.create(chatId, name, members)
        val currentUser = User(UserObject)
        members.add(currentUser)
        // create chat and save result of task in liveData
        _isChatCreatingSuccessfulLiveData.postValue(FirebaseObject.createChat(ChatObject))
    }

    // gets list of users that are contained in DB and local device
    @RequiresApi(Build.VERSION_CODES.O)     // to use cursor
    suspend fun getUsers(){
        val usersList: MutableList<User> = mutableListOf()
        val contactsNumberList = getContactList()                 // list of all local contacts
        val registerUsersList = mutableListOf<User>()

        // retrieve all users from DB
        Firebase.firestore.collection(FirebaseObject.COLLECTION_USERS)
            .get()
            .addOnSuccessListener { list ->
                for (user in list){
                    usersList.add(user.toObject(User::class.java))
                }
                Log.d("USERLIST", "getUsers is SUCCESSFUL")
            }
            .addOnFailureListener { Log.d("USERLIST", "getUsers() is FAILED ${it.message}") }
            .await()

        // compare contacts from device with users
        if (contactsNumberList.isNotEmpty()){
            for (number in contactsNumberList){
                for (user in usersList){
                    if (number == user.phone){
                        registerUsersList.add(user)
                    }
                }
            }
        }
        Log.d("USERLIST", "getUsers() phoneNumber $registerUsersList")
        _registerUsersListLiveData.postValue(registerUsersList)         // set list in liveData to show in recyclerView

    }

    // gets phone numbers of all contacts from device
    @RequiresApi(Build.VERSION_CODES.O)
    fun getContactList(): List<String> {
        val contactNumbersList: MutableList<String> = mutableListOf()
        val resolver = APP_ACTIVITY.contentResolver
        val contacts = resolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null, null, null)       // cursor
        while (contacts!!.moveToNext()){
            val phoneNumber = contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            contactNumbersList.add(phoneNumber.replace(" ", ""))     // add every number (without backspaces) in list
            Log.d("USERLIST", "getContactsList() phoneNumber $phoneNumber")
        }
        contacts.close()        // close cursor
        Log.d("USERLIST", "getContactsList() phoneNumber $contactNumbersList")
        return contactNumbersList
    }


}