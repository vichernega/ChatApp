package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatapp.databinding.ActivityMainBinding
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.example.chatapp.utilits.REGISTER_ACTIVITY
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.view.ChatsFragment
import com.example.chatapp.view.GetStartedFragment

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)       // binding initializing
        setContentView(binding.root)

        REGISTER_ACTIVITY = this
        setStatusBarParams()
        replaceFragment(GetStartedFragment())
    }


    //status bar appearance
    fun setStatusBarParams(){
        //black icons color
        val view: View = window.decorView
        view.setSystemUiVisibility(view.getSystemUiVisibility() or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        // white background color
        window.statusBarColor = this.resources.getColor(R.color.white)
    }

    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.activity_container)

        if (currentFragment != null) {
            // if current fragment is ChatsFragment app closes
            if (currentFragment is GetStartedFragment) {
                super.onBackPressed()
                finish()
            } else {
                super.onBackPressed()
            }
        }
    }
}