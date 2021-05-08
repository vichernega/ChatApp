package com.example.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.R
import com.example.chatapp.`object`.UserObject
import com.example.chatapp.databinding.FragmentChangePersonalInfoBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.replaceFragmentWithNoBackStack
import com.example.chatapp.utilits.showToast
import com.example.chatapp.viewmodel.ChangePersonalInfoViewModel

class ChangePersonalInfoFragment : Fragment(R.layout.fragment_change_personal_info) {

    private lateinit var binding: FragmentChangePersonalInfoBinding
    private val viewModel by viewModels<ChangePersonalInfoViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentChangePersonalInfoBinding.inflate(layoutInflater, container, false)

        APP_ACTIVITY.setToolbarTitle("")
        APP_ACTIVITY.showNavigationIcon()
        showUserInfo()
        setUpClickListeners()
        observeViewModel()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        APP_ACTIVITY.hideNavigationIcon()
    }

    fun showUserInfo(){
        binding.etFirstName.setText(UserObject.firstName)
        binding.etLastName.setText(UserObject.lastName)
        binding.etPhoneNumber.setText(UserObject.phone)
    }

    fun setUpClickListeners(){
        binding.btnSave.setOnClickListener {
            if (checkEditTexts()){
                viewModel.saveUserInfo(binding.etFirstName.text.toString().trim { it <= ' ' },
                        binding.etLastName.text.toString().trim { it <= ' ' },
                        binding.etPhoneNumber.text.toString().trim { it <= ' ' })
            }
        }
    }

    fun observeViewModel(){
        viewModel.isSaved.observe(viewLifecycleOwner, Observer {
            if (it == true){
                showToast("Saved")
                replaceFragmentWithNoBackStack(SettingsFragment())
            }
        })
    }

    fun checkEditTexts():Boolean{           // returns true if user info is correct

        var result: Boolean = true

        // First name edit text
        if (binding.etFirstName.text.toString().trim { it <= ' ' }.isEmpty()){      // check is empty
            binding.tilFirstName.error = "Fill in!"
            result = false
        }

        // Last name edit text
        if (binding.etLastName.text.toString().trim { it <= ' ' }.isEmpty()){     // check is empty
            binding.tilLastName.error = "Fill in!"
            result = false
        }

        // Phone number edit text
        if (binding.etPhoneNumber.text.toString().trim { it <= ' ' }.isEmpty()){          // check is empty
            binding.tilPhoneNumber.error = "Fill in!"
            result = false
        }

        return result
    }
}