package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatapp.utilits.REGISTER_ACTIVITY
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.view.GetStartedFragment

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
}