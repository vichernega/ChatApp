package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.chatapp.R
import com.example.chatapp.`object`.Chat
import com.example.chatapp.adapter.ChatsRecyclerViewAdapter
import com.example.chatapp.databinding.FragmentChatsBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.viewmodel.ChatsViewModel


class ChatsFragment : Fragment(R.layout.fragment_chats) {

    private lateinit var binding: FragmentChatsBinding
    private val viewModel by viewModels<ChatsViewModel>()
    private lateinit var recyclerViewAdapter: ChatsRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatsBinding.inflate(layoutInflater, container, false)
        APP_ACTIVITY.showCreateChatIcon()

        observeViewModel()
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getChats()          // get list of chats from DB
    }

    fun observeViewModel(){
        viewModel.chatsListLiveData.observe(viewLifecycleOwner, Observer {
            // chatsList is empty -> show empty message
            if (it.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
            // chatsList is not empty -> show it in recycler view
            else {
                binding.progressBar.visibility = View.GONE
                initRecyclerView(it)
            }
        })
    }

    fun initRecyclerView(chatsList: MutableList<Chat>){
        recyclerViewAdapter = ChatsRecyclerViewAdapter(chatsList)
        binding.chatsRecyclerView.adapter = recyclerViewAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        APP_ACTIVITY.hideCreateChatIcon()
    }

}