package com.example.chatapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.chatapp.`object`.FirestoreObject
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.replaceActivity
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.view.ChatsFragment
import com.example.chatapp.view.ContactsFragment
import com.example.chatapp.view.SettingsFragment
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)       // binding initializing
        setContentView(binding.root)

        APP_ACTIVITY = this
        if(FirebaseAuth.getInstance().currentUser == null){
            replaceActivity(RegisterActivity())
        } else {
            replaceFragment(ChatsFragment())
        }
        setStatusBarParams()
    }

    override fun onStart() {
        super.onStart()

        hideNavigationIcon()

        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_contacts -> { replaceFragment(ContactsFragment())
                                    setToolbarTitle("Contacts")
                                    hideNavigationIcon() }
                R.id.nav_chats -> { replaceFragment(ChatsFragment())
                                    setToolbarTitle("Chats")
                                    hideNavigationIcon() }
                R.id.nav_settings -> { replaceFragment(SettingsFragment())
                                    setToolbarTitle("Settings")
                                    hideNavigationIcon() }
            }
            true
        }
    }


    //status bar appearance
    fun setStatusBarParams(){
        //black icons color
        val view: View = window.decorView
        view.setSystemUiVisibility(view.getSystemUiVisibility() or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        // white background color
        window.statusBarColor = this.resources.getColor(R.color.white)
    }

    fun setToolbarTitle(title: String){
        binding.toolbar.title = title
    }

    fun hideNavigationIcon(){
        binding.toolbar.navigationIcon = null
    }
    fun showNavigationIcon(){
        binding.toolbar.navigationIcon = getDrawable(R.drawable.ic_arrow_back)
    }
}