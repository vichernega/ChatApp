package com.example.chatapp.other

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class CommonMessageViewHolder<in T> (view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item : T)
}