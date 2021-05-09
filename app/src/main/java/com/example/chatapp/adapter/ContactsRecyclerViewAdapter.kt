package com.example.chatapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.`object`.FirebaseObject.FOLDER_IMAGES
import com.example.chatapp.`object`.User
import com.example.chatapp.databinding.ContactsRecyclerViewItemBinding
import com.example.chatapp.utilits.APP_ACTIVITY
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

/** ADAPTER gets the data, adapts it to recyclerView and shows to user*/
class ContactsRecyclerViewAdapter(private val userList: List<User>):
    RecyclerView.Adapter<ContactsRecyclerViewAdapter.ContactsViewHolder>(){

    /** initialize recycler item components*/
    class ContactsViewHolder(val binding: ContactsRecyclerViewItemBinding) : RecyclerView.ViewHolder(binding.root)

    /** inflates the recycler item to recycler view*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ContactsRecyclerViewItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    /**sets data from productList to components in recyclerView_item*/
    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {

        // set profile image
        if (userList[position].image.isNotEmpty()){
            // image reference
            val imageRef = Firebase.storage.reference
                .child(FOLDER_IMAGES + userList[position].id + "/" + userList[position].image)

            imageRef.downloadUrl                            // get image uri
                .addOnSuccessListener {
                    Glide.with(APP_ACTIVITY)                // load image from DB and show in ImageView
                        .load(it)
                        .into(holder.binding.profileImage)
                    Log.d("IMAGE", "getUserProfileImage1() is SUCCESSFUL")
                }
                .addOnFailureListener { Log.d("IMAGE", "getUserProfileImage1() is FAILED: ${it.message}") }
        }

        // set full name
        holder.binding.tvFullName.setText("${userList[position].firstName} ${userList[position].lastName}")
        // set phone number
        holder.binding.tvPhoneNumber.setText(userList[position].phone)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}