package com.example.calendarandtasks.util.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.calendarandtasks.data.model.Task
import com.example.calendarandtasks.data.model.TaskDiffCallback
import com.example.calendarandtasks.databinding.TaskItemLayoutBinding

class TaskTodayAdapter : ListAdapter<Task, TaskTodayViewHolder>(TaskDiffCallback)  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskTodayViewHolder {
        return TaskTodayViewHolder(TaskItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: TaskTodayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


}