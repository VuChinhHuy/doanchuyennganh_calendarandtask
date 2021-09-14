package com.example.calendarandtasks.util.adapter.TaskAttachment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.databinding.AttachmentDetailsItemBinding

class TaskDetailsAttachmentAdapter : RecyclerView.Adapter<TaskDetailsAttachmentHolder>() {

    private var list : List<DetailsTask> = emptyList()
    fun submisList(detailsAttachment : List<DetailsTask>){
        list = detailsAttachment
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskDetailsAttachmentHolder {
        return TaskDetailsAttachmentHolder(AttachmentDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: TaskDetailsAttachmentHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }
}