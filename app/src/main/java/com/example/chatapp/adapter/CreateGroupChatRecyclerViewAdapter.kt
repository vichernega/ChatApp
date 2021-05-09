package com.example.chatapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.`object`.FirebaseObject
import com.example.chatapp.`object`.User
import com.example.chatapp.databinding.CreateGroupItemRecyclerViewBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/** ADAPTER gets the data, adapts it to recyclerView and shows to user*/
class CreateGroupChatRecyclerViewAdapter(private val userList: List<User>, private val onCheckboxClick: OnCheckboxClickListener):
    RecyclerView.Adapter<CreateGroupChatRecyclerViewAdapter.CreateGroupChatViewHolder>(){

    /** initialize recycler item components*/
    class CreateGroupChatViewHolder(val binding: CreateGroupItemRecyclerViewBinding, onCheckboxClick: OnCheckboxClickListener) :
        RecyclerView.ViewHolder(binding.root) {
            init {
                binding.checkbox.setOnClickListener {
                    if (binding.checkbox.isChecked){        // fragment reacts on clicks
                        onCheckboxClick.onClick(absoluteAdapterPosition, true)
                    } else {
                        onCheckboxClick.onClick(absoluteAdapterPosition, false)
                    }
                }
            }
        }

    /** inflates the recycler item to recycler view*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreateGroupChatViewHolder {
        return CreateGroupChatViewHolder(
            CreateGroupItemRecyclerViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), onCheckboxClick )
    }

    /**sets data from productList to components in recyclerView_item*/
    override fun onBindViewHolder(holder: CreateGroupChatViewHolder, position: Int) {
        // set profile image
        if (userList[position].image.isNotEmpty()){
            // image reference
            val imageRef = Firebase.storage.reference
                .child(FirebaseObject.FOLDER_IMAGES + userList[position].id + "/" + userList[position].image)

            imageRef.downloadUrl                            // get image uri
                .addOnSuccessListener {
                    Glide.with(APP_ACTIVITY)                // load image from DB and show in ImageView
                        .load(it)
                        .into(holder.binding.profileImage)
                    Log.d("IMAGE", "getUserProfileImage() is SUCCESSFUL")
                }
                .addOnFailureListener { Log.d("IMAGE", "getUserProfileImage() is FAILED: ${it.message}") }
        }

        // set full name
        holder.binding.tvFullName.setText("${userList[position].firstName} ${userList[position].lastName}")
        // set phone number
        holder.binding.tvPhoneNumber.setText(userList[position].phone)
    }

    override fun getItemCount(): Int = userList.size


    // interface that provides reaction on checkbox click
    interface OnCheckboxClickListener{
        fun onClick(position: Int, isChecked: Boolean)
    }
}