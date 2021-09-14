package com.example.calendarandtasks.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.calendarandtasks.data.firebase.grouptask.GroupTaskRepository

class DeleteGroupWorker(
    context : Context,
    workerParams: WorkerParameters
): CoroutineWorker(context,workerParams) {

    private val repository = GroupTaskRepository()
    private val context = applicationContext
    override suspend fun doWork(): Result {
        val groupId = checkNotNull(inputData.getString(KEY_GROUP_ID))

        when(repository.deleteGroup(groupId)){
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
        var KEY_GROUP_ID = "key-title-new-group"
    }
}