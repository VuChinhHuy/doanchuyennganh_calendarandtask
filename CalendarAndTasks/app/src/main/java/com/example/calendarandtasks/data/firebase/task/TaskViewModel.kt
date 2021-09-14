package com.example.calendarandtasks.data.firebase.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import com.example.calendarandtasks.data.firebase.Result
import java.lang.Exception
import com.example.calendarandtasks.data.model.Task

class TaskViewModel : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    private val repository : TaskRepository = TaskRepository()
    fun loadTaskInGroup(groupId: String) : LiveData<Result<List<Task>>> = liveData(coroutineContext){
        emit(Result.Loading)
        val result = repository.getTask(groupId)
        if(result is Result.Success && result.data.isEmpty()){
            emit(Result.Error(Exception("There is no tasks at the moment.\n Wait for it.")))
        }
        else emit(result)
    }
    fun newTask(groupId: String, task: com.example.calendarandtasks.data.model.Task) :LiveData<Result<Boolean>>  = liveData(coroutineContext){
        emit(Result.Loading)
        emit(repository.newTask(groupId,task))
    }
    fun deleteTask(groupId: String,taskId : String) : LiveData<Result<Boolean>> = liveData(coroutineContext){
        emit(Result.Loading)
        emit(repository.deleteTask(groupId,taskId))
    }
    fun getTaskToday() : LiveData<Result<List<Task>>> = liveData(coroutineContext){
        emit(Result.Loading)
        val result = repository.getTaskToDay()
        if(result is Result.Success && result.data.isEmpty()){
            emit(Result.Error(Exception("There is no tasks at the moment.\n Wait for it.")))
        }
        else emit(result)
    }
}