package com.example.chatapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.`object`.*
import com.example.chatapp.`object`.FirebaseObject.FOLDER_IMAGES
import com.example.chatapp.databinding.ChatsRecyclerViewItemBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.example.chatapp.utilits.replaceFragment
import com.example.chatapp.view.ChatFragment
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/** ADAPTER gets the data, adapts it to recyclerView and shows to user*/
class ChatsRecyclerViewAdapter(private val chatsList: MutableList<Chat>):
    RecyclerView.Adapter<ChatsRecyclerViewAdapter.ChatsViewHolder>() {

    /** initialize recycler item components*/
    class ChatsViewHolder(val binding: ChatsRecyclerViewItemBinding): RecyclerView.ViewHolder(binding.root)

    /** inflates the recycler item to recycler view*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        return ChatsViewHolder(
            ChatsRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**sets data from productList to components in recyclerView_item*/
    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        // set as profile image first member of the group (or another member of chat)
        var member: User? = null
        for (mem in chatsList[position].members){
            if (mem.id != UserObject.id){
                member = mem     // first member of chat
                break
            }
        }

        // set user image (get it from db)
        if (member!!.image.isNotEmpty()){
            FirebaseObject.downloadImage(member.id, member.image, holder.binding.profileImage)
        }

        if (chatsList[position].members.size == 2){     // setting full name as title
            holder.binding.tvChatName.setText(member.firstName + " " + member.lastName)
        } else {                                        // setting conversation name as title
            holder.binding.tvChatName.setText(chatsList[position].name)
        }

        // on chat click
        holder.binding.chatContainer.setOnClickListener {
            ChatObject.set(chatsList[position])          // set id to chat object to open chat in ChatFragment
            APP_ACTIVITY.replaceFragment(ChatFragment())
        }
    }

    override fun getItemCount(): Int {
        return chatsList.size
    }

}