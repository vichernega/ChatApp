package com.example.chatapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatapp.R
import com.example.chatapp.databinding.FragmentChatsBinding
import com.example.chatapp.utilits.APP_ACTIVITY


class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var binding: FragmentChatsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        APP_ACTIVITY.showCreateChatIcon()
        return binding.root
    }

    fun setUpClickListeners(){

    }

    override fun onDestroy() {
        super.onDestroy()
        APP_ACTIVITY.hideCreateChatIcon()
    }

}