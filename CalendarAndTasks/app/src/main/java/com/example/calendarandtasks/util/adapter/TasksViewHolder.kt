package com.example.calendarandtasks.util.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.calendarandtasks.R
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.data.model.Task
import com.example.calendarandtasks.databinding.TaskItemLayoutBinding
import com.example.calendarandtasks.util.adapter.TaskAttachment.TaskDetailsAttachmentAdapter

class TasksViewHolder (val binding: TaskItemLayoutBinding,
                              listener: TasksAdapter.TasksAdapterListener
): RecyclerView.ViewHolder(binding.root){

    init {

        binding.run {
            this.listener = listener
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
    fun bind(tasks : Task){
        binding.tasks = tasks
    }
    fun bindDetails(tasks: Task){
        val adapter = TaskDetailsAttachmentAdapter()
        adapter.submisList(tasks.detailsTask)
        binding.recDetails.adapter = adapter
    }

}
