package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentSignUpBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.REGISTER_ACTIVITY
import com.example.chatapp.utilits.replaceActivity
import com.example.chatapp.viewmodel.SignUpViewModel

class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)

        setUpClickListeners()
        observeViewModel()

        return binding.root
    }

    fun observeViewModel(){
        viewModel.userLiveData.observe(viewLifecycleOwner, Observer {
            REGISTER_ACTIVITY.replaceActivity(APP_ACTIVITY)
        })
    }

    fun setUpClickListeners(){
        binding.btnSignUp.setOnClickListener {              //sign up if user data is correct
            if (checkEditTexts()){
                viewModel.signUp(binding.etFirstName.text.toString().trim { it <= ' ' },
                        binding.etLastName.text.toString().trim { it <= ' ' },
                        binding.etPhoneNumber.text.toString().trim { it <= ' ' },
                        binding.etEmail.text.toString().trim { it <= ' ' },
                        binding.etPassword.text.toString().trim { it <= ' ' })
            }
        }
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

        // Email edit text
        if (binding.etEmail.text.toString().trim { it <= ' ' }.isEmpty()){          // check is empty
            binding.tilEmail.error = "Fill in!"
            result = false
        }
        else if (!(binding.etEmail.text.toString().contains('@') &&
            binding.etEmail.text.toString().contains('.'))){                    // check is valid
            binding.tilEmail.error = "Invalid email!"
            result = false
        }

        // Password edit text
        if (binding.etPassword.text.toString().trim { it <= ' ' }.isEmpty()){          // check is empty
            binding.tilPassword.error = "Fill in!"
            result = false
        }
        else if (binding.etEmail.text.toString().trim { it <= ' ' }.length < 8){                    // check is valid
            binding.tilPassword.error = "Password is too short!"
            result = false
        }

        return result
    }

}