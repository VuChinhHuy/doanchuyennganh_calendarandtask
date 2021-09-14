package com.example.calendarandtasks.data.model

import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.firestore.DocumentId

data class Task(
    @DocumentId
    val id: String,
    val titleTask: String,
    val note: String,
    val timeStart: String,
    val dateStart: String,
    val timeEnd: String,
    val dateEndt: String,
    val notification: Boolean = true,
    val location: String,
    val isFinished: Boolean = false,
    val detailsTask: ArrayList<DetailsTask>

){
    @Suppress("unused")
    constructor() : this("", "","","","","","",true,"",false,ArrayList())

}

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
}