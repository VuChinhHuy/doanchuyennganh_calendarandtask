package com.example.calendarandtasks.util.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.data.model.Group
import com.example.calendarandtasks.databinding.GroupTaskItemLayoutBinding

class GroupViewHolder(
    private val binding: GroupTaskItemLayoutBinding,
    listener: GroupAdapter.GroupAdapterListener
): RecyclerView.ViewHolder(binding.root){
    init {
        binding.run {
            this.listener = listener
        }
    }
    @SuppressLint("ResourceType")
    fun bind(group : Group){
        binding.group = group
        binding.image.setImageResource(group.imgIcon)
    }
}