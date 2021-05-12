package com.example.chatapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.replaceActivity
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.utilits.replaceFragmentWithNoBackStack
import com.example.chatapp.view.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)       // binding initializing
        setContentView(binding.root)

        APP_ACTIVITY = this
        if(FirebaseAuth.getInstance().currentUser == null){         // Registration activity if user is null
            replaceActivity(RegisterActivity())
        } else {
            replaceFragmentWithNoBackStack(ChatsFragment())         // show ChatsFragment as first in
            setToolbarTitle("Chats")
            binding.bottomNavBar.selectedItemId = R.id.nav_chats
            lifecycleScope.launch(Dispatchers.IO){
                FirebaseObject.getUser()                           // get user info from DB to show it in SettingsFragment
            }
        }
        setStatusBarParams()
        setOnBackIconClick()
    }

    override fun onStart() {
        super.onStart()

        hideNavigationIcon()

        /**Bottom navigation bar on item click*/
        binding.bottomNavBar.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_contacts -> { replaceFragmentWithNoBackStack(ContactsFragment())
                                    setToolbarTitle("Contacts")
                                    hideNavigationIcon() }
                R.id.nav_chats -> { replaceFragmentWithNoBackStack(ChatsFragment())
                                    setToolbarTitle("Chats")
                                    hideNavigationIcon() }
                R.id.nav_settings -> { replaceFragmentWithNoBackStack(SettingsFragment())
                                    setToolbarTitle("Settings")
                                    hideNavigationIcon() }
            }
            true
        }
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.activity_container)

        if (currentFragment != null){
            // if current fragment is ChatsFragment app closes
            if (binding.bottomNavBar.selectedItemId == R.id.nav_chats && currentFragment is ChatsFragment){
                super.onBackPressed()
                finish()
            }
            // if current fragment is ChatsFragment or SettingsFragment app closes
            else if (currentFragment is ContactsFragment || currentFragment is SettingsFragment){
                binding.bottomNavBar.selectedItemId = R.id.nav_chats
            }
            else if (currentFragment is ChatFragment) {
                replaceFragmentWithNoBackStack(ChatsFragment())
                showBottomnavBar()
                binding.bottomNavBar.selectedItemId = R.id.nav_chats
            }
            // in another case user navigates by back stack
            else {
                super.onBackPressed()
            }
        }
    }

    fun setOnBackIconClick(){
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
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
    fun hideCreateChatIcon(){
        val createChatIcon = binding.toolbar.menu.findItem(R.id.add_chat)
        createChatIcon.isVisible = false
    }
    fun showCreateChatIcon(){
        val createChatIcon = binding.toolbar.menu.findItem(R.id.add_chat)
        createChatIcon.isVisible = true

        createChatIcon.setOnMenuItemClickListener {
            replaceFragment(CreateGroupChatFragment())
            true
        }
    }
    fun hideBottomNavBar(){
        binding.bottomNavBar.visibility = View.GONE
    }
    fun showBottomnavBar(){
        binding.bottomNavBar.visibility = View.VISIBLE
    }
}