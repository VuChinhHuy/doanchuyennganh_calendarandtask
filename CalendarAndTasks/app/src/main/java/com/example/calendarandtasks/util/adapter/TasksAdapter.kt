package com.example.calendarandtasks.util.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.calendarandtasks.data.model.Group
import com.example.calendarandtasks.data.model.Task
import com.example.calendarandtasks.data.model.TaskDiffCallback
import com.example.calendarandtasks.databinding.TaskItemLayoutBinding
import com.orhanobut.logger.Logger

class TasksAdapter(
    private val listener: TasksAdapterListener
)
 : ListAdapter<Task,TasksViewHolder>(TaskDiffCallback) {
    interface TasksAdapterListener{
        fun onClicked(cardView : View, tasks: Task)
        fun onTaskLongPressed(cardView: View,tasks: Task) : Boolean
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder( TaskItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ),
            listener)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.bindDetails(getItem(position))
    }
}