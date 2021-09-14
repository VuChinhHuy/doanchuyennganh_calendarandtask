package com.example.calendarandtasks.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.calendarandtasks.data.firebase.task.TaskRepository
import com.example.calendarandtasks.data.model.DetailsTask
import com.example.calendarandtasks.data.model.Task

class NewTaskWorker(
    context : Context,
    workerParams: WorkerParameters
): CoroutineWorker(context,workerParams) {

    private val repository = TaskRepository()

    override suspend fun doWork(): Result {
        val title = checkNotNull(inputData.getString(KEY_TITLE))
        val groupId = checkNotNull(inputData.getString(KEY_GROUP_ID))
        val note = checkNotNull(inputData.getString(KEY_NOTE))
        val timeStart = checkNotNull(inputData.getString(KEY_TIME_START))
        val dateStart = checkNotNull(inputData.getString(KEY_DATE_START))
        val dateEnd = checkNotNull(inputData.getString(KEY_DATE_END))
        val timeEnd = checkNotNull(inputData.getString(KEY_TIME_END))
        val notification =  checkNotNull(inputData.getBoolean(KEY_NOTIFICATION,true))
        val location = checkNotNull(inputData.getString(KEY_LOCATION))
        val array = checkNotNull(inputData.getStringArray(KEY_DETAILS))

        val arrayDetailsTask = ArrayList<DetailsTask>()
        array.map {
            val de = DetailsTask(it)
            arrayDetailsTask.add(de)
        }

        val newTask = Task("",title,note,timeStart,dateStart,timeEnd,dateEnd,notification,location,false,arrayDetailsTask)
        when(repository.newTask(groupId,newTask)){
            is com.example.calendarandtasks.data.firebase.Result.Success ->{
                return Result.success()
            }
            is com.example.calendarandtasks.data.firebase.Result.Error -> {
                return Result.failure()
            }
        }
        return Result.failure()
    }
    companion object {
        var KEY_TITLE = "key-title-new-task"
        var KEY_GROUP_ID =  "key-group-id"
        var KEY_NOTE =  "key-note-new-task"
        var KEY_TIME_START =  "key-time-start-new-task"
        var KEY_DATE_START =  "key-date-start-new-task"
        var KEY_DATE_END =  "key-date-end-new-task"
        var KEY_TIME_END =  "key-time-end-new-task"
        var KEY_NOTIFICATION = "key-notification-new-task"
        var KEY_LOCATION = "key-location-new-task"
        var KEY_DETAILS = "key-details-new-task"

    }
}