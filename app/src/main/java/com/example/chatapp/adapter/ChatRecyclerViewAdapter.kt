package com.example.chatapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.FirebaseObject.FOLDER_IMAGES
import com.example.chatapp.`object`.MessageItem
import com.example.chatapp.databinding.MyMessageItemBinding
import com.example.chatapp.databinding.SenderMessageItemBinding
import com.example.chatapp.other.CommonMessageViewHolder
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.TYPE_MY_MESSAGE
import com.example.chatapp.utilits.TYPE_SENDER_MESSAGE
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/** ADAPTER gets the data, adapts it to recyclerView and shows to user*/
class ChatRecyclerViewAdapter(private val messageList: MutableList<MessageItem>):
    RecyclerView.Adapter<CommonMessageViewHolder<MessageItem>>() {

    /** inflates the recycler item to recycler view*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonMessageViewHolder<MessageItem> {
        return when (viewType) {
            // if message from local user -> use my_message_item.xml
            TYPE_MY_MESSAGE -> {
                val binding = MyMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MyMessageViewHolder(binding)
            }
            // if message from another user -> use sender_message_item.xml
            TYPE_SENDER_MESSAGE -> {
                val binding = SenderMessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                SenderMessageViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid viewBinding type")
        }
    }

    /**sets data from productList to components in recyclerView_item*/
    override fun onBindViewHolder(holder: CommonMessageViewHolder<MessageItem>, position: Int) {
        when (holder) {
            is MyMessageViewHolder -> holder.bind(messageList[position])
            is SenderMessageViewHolder -> holder.bind(messageList[position])
            else -> throw IllegalArgumentException()
        }
    }

    override fun getItemViewType(position: Int): Int = messageList[position].messageType

    override fun getItemCount(): Int {
        return messageList.size
    }



    /** initialize recycler item components to MY message*/
    class MyMessageViewHolder(val binding: MyMessageItemBinding): CommonMessageViewHolder<MessageItem>(binding.root){
        private val text = binding.text
        private val time = binding.time

        override fun bind(item: MessageItem) {
            time.text = item.time
            // if message has only text
            if (item.text.isNotEmpty()){
                text.text = item.text
            }
            // if message has only image download it and put in imageView
            else {
                binding.image.visibility = View.VISIBLE
                FirebaseObject.downloadImage(item.authorId, item.image, binding.image)
            }
        }
    }

    /** initialize recycler item components to SENDER message*/
    class SenderMessageViewHolder(val binding: SenderMessageItemBinding): CommonMessageViewHolder<MessageItem>(binding.root){
        private val author = binding.senderFullName
        private val text = binding.text
        private val time = binding.time

        override fun bind(item: MessageItem) {
            author.text = item.authorName
            time.text = item.time
            // if message has only text
            if (item.text.isNotEmpty()){
                text.text = item.text
            }
            // if message has only image download it and put in imageView
            else {
                binding.image.visibility = View.VISIBLE
                FirebaseObject.downloadImage(item.authorId, item.image, binding.image)
            }
        }
    }
}