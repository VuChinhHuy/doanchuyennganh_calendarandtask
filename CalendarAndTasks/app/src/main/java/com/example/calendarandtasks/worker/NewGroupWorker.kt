package com.example.calendarandtasks.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.calendarandtasks.data.firebase.grouptask.GroupTaskRepository
import com.example.calendarandtasks.data.model.Group

class NewGroupWorker(
    context : Context,
    workerParams: WorkerParameters
):CoroutineWorker(context,workerParams) {

    private val repository = GroupTaskRepository()
    private val context = applicationContext
    override suspend fun doWork(): Result {
        val title = checkNotNull(inputData.getString(KEY_TITLE))
        val count = checkNotNull(inputData.getInt(KEY_COUNT,0))
        val color = checkNotNull(inputData.getInt(KEY_COLOR,-272496))
        val icon = checkNotNull(inputData.getInt(KEY_ICON,2131230879))

        val newGroup = Group("",title,count,color,icon)
        when(repository.newGroup(newGroup)){
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
        var KEY_TITLE = "key-title-new-group"
        const val KEY_COUNT = "key-count-new-group"
        const val KEY_COLOR = "key-color-new-group"
        const val KEY_ICON = "key-icon-new-group"

    }
}