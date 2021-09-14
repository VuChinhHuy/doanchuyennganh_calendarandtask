package com.example.calendarandtasks.data.model

data class DetailsTask(
    val details : String,
    val isFinished : Boolean = false
){
    @Suppress("unused")
    constructor():this("")

}

