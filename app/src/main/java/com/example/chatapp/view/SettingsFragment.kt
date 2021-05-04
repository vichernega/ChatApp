package com.example.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.R
import com.example.chatapp.RegisterActivity
import com.example.chatapp.`object`.FirestoreObject
import com.example.chatapp.`object`.User
import com.example.chatapp.databinding.FragmentSettingsBinding
import com.example.chatapp.utilits.*
import com.example.chatapp.viewmodel.SettingsViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel by viewModels<SettingsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getUserInfo()     // get user info from DB to show it in fragment
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        showUserInfo()
        observeViewModel()
        setUpClickListeners()

        return binding.root
    }

    fun showUserInfo(){
        binding.tvFullName.text = User.firstName + " " + User.lastName
        binding.tvPhoneNumber.text = User.phone
        binding.tvEmail.text = User.email
    }

    fun observeViewModel(){
        //replace activity if user is logged out
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            APP_ACTIVITY.replaceActivity(RegisterActivity())
        })
    }

    fun setUpClickListeners(){

        binding.changePersonalInfoContainer.setOnClickListener {
            replaceFragment(ChangePersonalInfoFragment())
        }

        binding.logOutContainer.setOnClickListener {
            viewModel.logout()
        }
    }

}