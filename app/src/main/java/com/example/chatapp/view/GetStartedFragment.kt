package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentGetStartedBinding
import com.example.chatapp.utilits.replaceFragment

class GetStartedFragment : Fragment(R.layout.fragment_get_started) {

    private lateinit var binding: FragmentGetStartedBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGetStartedBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpClickListeners()
    }

    fun setUpClickListeners(){

        binding.btnGetStarted.setOnClickListener {
            replaceFragment(SignUpFragment())
        }
        binding.tvLogin.setOnClickListener {
            replaceFragment(LoginFragment())
        }
    }
}