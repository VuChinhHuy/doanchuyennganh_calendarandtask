package com.example.calendarandtasks.data.firebase.task

import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Task

interface Tasks {
    suspend fun getTask(groupId : String): com.example.calendarandtasks.data.firebase.Result<List<Task>>
    suspend fun newTask(groupId: String, task: com.example.calendarandtasks.data.model.Task): com.example.calendarandtasks.data.firebase.Result<Boolean>
    suspend fun deleteTask(groupId: String, taskId : String) : com.example.calendarandtasks.data.firebase.Result<Boolean>
    suspend fun getTaskToDay() : Result<List<Task>>
}