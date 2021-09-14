package com.example.calendarandtasks.data.model


import androidx.recyclerview.widget.DiffUtil
import com.google.firebase.firestore.DocumentId

data class Group(
    @DocumentId
    val id: String,
    val titleGroup: String,
    val sumTask: Int = 0,
    val color: Int = 0,
    val imgIcon : Int = 0

    ) {
    @Suppress("unused")
    constructor() : this("", "", 0, -272496,2131230879)
}
object GroupDiffCallback : DiffUtil.ItemCallback<Group>() {
    override fun areItemsTheSame(oldItem: Group, newItem: Group) = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Group, newItem: Group) = oldItem == newItem
}