package com.example.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentLoginBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.REGISTER_ACTIVITY
import com.example.chatapp.utilits.replaceActivity
import com.example.chatapp.viewmodel.LoginViewModel

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

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
        binding.btnLogIn.setOnClickListener {
            if (checkEditTexts()){
                viewModel.signUp(binding.etEmail.text.toString().trim { it <= ' ' },
                    binding.etPassword.text.toString().trim { it <= ' ' })
            }
        }
    }

    fun checkEditTexts():Boolean{       // returns true if user info is correct

        var result: Boolean = true

        // Email edit text
        if (binding.etEmail.text.toString().trim { it <= ' ' }.isEmpty()){      // check is empty
            binding.tilEmail.error = "Fill in!"
            result = false
        }
        else if (!(binding.etEmail.text.toString().contains('@') &&
                    binding.etEmail.text.toString().contains('.'))){       // check is valid
            binding.tilEmail.error = "Invalid email!"
            result = false
        }

        // Password edit text
        if (binding.etPassword.text.toString().trim { it <= ' ' }.isEmpty()){       // check is empty
            binding.tilPassword.error = "Fill in!"
            result = false
        }

        return result
    }
}