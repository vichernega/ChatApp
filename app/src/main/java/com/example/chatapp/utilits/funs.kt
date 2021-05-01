package com.example.chatapp.utilits

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.chatapp.R

// file where different functions are inflated*/


fun Fragment.replaceFragment(fragment: Fragment){
    fragmentManager?.beginTransaction()
        ?.replace(R.id.activity_container, fragment)
        ?.addToBackStack(null)
        ?.commit()

}

fun Fragment.replaceFragmentWithNoBackStack(fragment: Fragment){
    fragmentManager?.beginTransaction()
        ?.replace(R.id.activity_container, fragment)
        ?.commit()
}

fun AppCompatActivity.replaceFragment(fragment: Fragment){
    supportFragmentManager.beginTransaction()
        .replace(R.id.activity_container, fragment)
        .addToBackStack(null)
        .commit()
}

fun AppCompatActivity.replaceFragmentWithNoBackStack(fragment: Fragment){
    supportFragmentManager.beginTransaction()
        .replace(R.id.activity_container, fragment)
        .commit()
}

fun AppCompatActivity.replaceActivity(activity: AppCompatActivity){
    val intent = Intent(this, activity::class.java)
    startActivity(intent)
    this.finish()
}

fun showToast(str: String){
    Toast.makeText(APP_ACTIVITY, str, Toast.LENGTH_SHORT).show()
}

