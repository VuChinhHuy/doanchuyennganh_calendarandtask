package com.example.calendarandtasks.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.calendarandtasks.data.firebase.task.TaskRepository

class DeleteTaskWorker (
    context : Context,
    workerParams: WorkerParameters
): CoroutineWorker(context,workerParams) {

    private val repository = TaskRepository()
    private val context = applicationContext
    override suspend fun doWork(): Result {
        val groupId = checkNotNull(inputData.getString(KEY_GROUP_ID))
        val taskId = checkNotNull(inputData.getString(KEY_TASK_ID))

        when(repository.deleteTask(groupId,taskId)){
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
        var KEY_TASK_ID = "key-id-task"
        var KEY_GROUP_ID = "key-id-group"
    }
}