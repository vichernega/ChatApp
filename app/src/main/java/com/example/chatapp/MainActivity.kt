package com.example.chatapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.replaceActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        APP_ACTIVITY = this
        if(FirebaseAuth.getInstance().currentUser == null){
            replaceActivity(RegisterActivity())
        }
        replaceActivity(RegisterActivity())
        setStatusBarParams()
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