package com.example.chatapp.model

import android.os.Build
import android.provider.ContactsContract
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.example.chatapp.`object`.FirebaseObject.COLLECTION_USERS
import com.example.chatapp.`object`.User
import com.example.chatapp.utilits.APP_ACTIVITY
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ContactsRepository {

    private var _registerUsersList: MutableLiveData<List<User>> = MutableLiveData()
    val registerUserList get() = _registerUsersList

    // gets list of users that are contained in DB and local device
    @RequiresApi(Build.VERSION_CODES.O)     // to use cursor
    suspend fun getUsers(){
        val usersList: MutableList<User> = mutableListOf()
        val contactsNumberList = getContactList()                 // list of all local contacts
        val registerUsersList = mutableListOf<User>()

        // retrieve all users from DB
        Firebase.firestore.collection(COLLECTION_USERS)
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
        _registerUsersList.postValue(registerUsersList)         // set list in liveData to show in recyclerView

    }

    // gets phone numbers of all contacts from device
    @RequiresApi(Build.VERSION_CODES.O)
    fun getContactList(): List<String> {
        val contactNumbersList: MutableList<String> = mutableListOf()
        val resolver = APP_ACTIVITY.contentResolver
        val contacts = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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