package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.RegisterActivity
import com.example.chatapp.`object`.ImageObject
import com.example.chatapp.`object`.UserObject
import com.example.chatapp.databinding.FragmentSettingsBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.TAG_SET_PROFILE_IMAGE
import com.example.chatapp.utilits.replaceActivity
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.viewmodel.SettingsViewModel


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)

        showUserInfo()
        observeViewModel()
        setUpClickListeners()

        return binding.root
    }

    fun showUserInfo(){
        binding.tvFullName.text = UserObject.firstName + " " + UserObject.lastName
        binding.tvPhoneNumber.text = UserObject.phone
        binding.tvEmail.text = UserObject.email
        viewModel.getProfileImage()         // download profile image and set in observeLiveData
    }

    fun observeViewModel(){
        //replace activity if user is logged out
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            APP_ACTIVITY.replaceActivity(RegisterActivity())
        })

        //get user photo
        viewModel.isProfileImageDownloadedLiveData.observe(viewLifecycleOwner, Observer { downloaded ->
            if (downloaded){                    // if photo is downloaded from DB show it in fragment with Glide help
                Glide.with(APP_ACTIVITY)
                    .load(ImageObject.uri)
                    .into(binding.profileImage)
            }
        })
    }

    fun setUpClickListeners(){

        // show Dialog Fragment after click on profileImage
        binding.profileImage.setOnClickListener {
            ChooseImageActionDialogFragment().show(parentFragmentManager, TAG_SET_PROFILE_IMAGE)
        }
        // on changePersonalInfo click
        binding.changePersonalInfoContainer.setOnClickListener {
            replaceFragment(ChangePersonalInfoFragment())
        }
        // on logout click
        binding.logOutContainer.setOnClickListener {
            viewModel.logout()
        }
    }

}