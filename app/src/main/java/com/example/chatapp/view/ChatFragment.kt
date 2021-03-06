package com.example.chatapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.chatapp.R
import com.example.chatapp.`object`.ChatObject
import com.example.chatapp.`object`.MessageItem
import com.example.chatapp.`object`.UserObject
import com.example.chatapp.adapter.ChatRecyclerViewAdapter
import com.example.chatapp.databinding.FragmentChatBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.viewmodel.ChatViewModel

class ChatFragment : Fragment(R.layout.fragment_chat) {

    private lateinit var binding: FragmentChatBinding
    private val viewModel: ChatViewModel by activityViewModels()
    private lateinit var recyclerViewAdapter: ChatRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        // appearance of fragment
        APP_ACTIVITY.hideBottomNavBar()
        APP_ACTIVITY.hideCreateChatIcon()
        APP_ACTIVITY.showNavigationIcon()

        setTitle()
        setUpClickListeners()
        observeViewModel()

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getChat()     // get all user chats from db
    }

    fun initRecyclerViewAdapter(messageList: MutableList<MessageItem>){
        recyclerViewAdapter = ChatRecyclerViewAdapter(messageList)
        binding.chatRecyclerView.adapter = recyclerViewAdapter
    }

    fun observeViewModel(){

        // List of user chats
        viewModel.messagesListLiveData.observe(viewLifecycleOwner, {
            if (it.isEmpty()){
                binding.tvEmpty.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                initRecyclerViewAdapter(it)
            }
        })
    }

    fun setUpClickListeners(){

        // btn to add picture
        binding.btnAttachFile.setOnClickListener {
            ChooseImageActionDialogFragment().show(parentFragmentManager, "chooseImageAction")
        }

        // btn to send text message
        binding.btnSend.setOnClickListener {
            Log.d("MESSAGE", "IN listener" + binding.input.toString().trim{ it <= ' '})
            if (readInput().isNotEmpty()){
                viewModel.sendText(readInput())
                binding.input.setText("")
            }
        }
    }

    // get text message from edit text
    fun readInput(): String {
        Log.d("MESSAGE", binding.input.text.toString().trim{ it <= ' '})
        return binding.input.text.toString().trim{ it <= ' '}
    }

    fun setTitle(){
        // if chat is between 2 users -> show name of sender
        if (ChatObject.members.size == 2){
            for (member in ChatObject.members){
                if (member.id != UserObject.id){
                    APP_ACTIVITY.setToolbarTitle(member.firstName + " " + member.lastName)
                }
            }
        }
        // group chat -> show chat name
        else APP_ACTIVITY.setToolbarTitle(ChatObject.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clearList()
    }

}