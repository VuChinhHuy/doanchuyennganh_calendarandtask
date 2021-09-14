package com.example.calendarandtasks.util.adapter.TaskAttachment

import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.databinding.AttachmentDetailsItemBinding

class TaskDetailsAttachmentHolder(
    private val binding : AttachmentDetailsItemBinding
)
    : RecyclerView.ViewHolder(binding.root) {

        fun bind(details: DetailsTask){
            binding.titleDetail.text = details.details
        }
}