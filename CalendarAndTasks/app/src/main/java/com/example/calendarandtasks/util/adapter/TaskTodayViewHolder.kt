package com.example.calendarandtasks.util.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.model.Task
import com.example.calendarandtasks.databinding.TaskItemLayoutBinding
import com.example.calendarandtasks.util.adapter.TaskAttachment.TaskDetailsAttachmentAdapter

class TaskTodayViewHolder(val binding: TaskItemLayoutBinding,
): RecyclerView.ViewHolder(binding.root){

    init {
        binding.run {
            btnUpDown.setOnClickListener {
                if (binding.rec.visibility == View.GONE) {
                    rec.visibility = View.VISIBLE
                    btnUpDown.setIconResource(R.drawable.ic_keyboard_arrow_up)
                } else {
                    rec.visibility = View.GONE
                    btnUpDown.setIconResource(R.drawable.ic_keyboard_arrow_down)
                }
            }
        }
    }
    fun bind(task : Task){
        binding.tasks = task
        val adapter = TaskDetailsAttachmentAdapter()
        adapter.submisList(task.detailsTask)
        binding.recDetails.adapter = adapter
    }
}