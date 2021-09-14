package com.example.calendarandtasks.data.firebase.grouptask

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Group
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class GroupTasksViewModel : ViewModel() {
    private val coroutineContext = viewModelScope.coroutineContext + Dispatchers.IO
    private var repository : GroupTaskRepository = GroupTaskRepository()
    fun loadGroup() : LiveData<Result<List<Group>>> = liveData(coroutineContext) {
        emit(Result.Loading)
        val result= repository.getGroupTask()

        if(result is Result.Success && result.data.isEmpty()){
            emit(Result.Error(Exception("There is no group task at the moment.\n Wait for it.")))
        }
        else
            emit(result)
    }
    fun newGroup(group: Group): LiveData<Result<Boolean>> = liveData(coroutineContext) {
        emit(Result.Loading)
        emit(repository.newGroup(group))
    }
    fun deleteGroup(groupId: String) : LiveData<Result<Boolean>> = liveData(coroutineContext){
        emit(Result.Loading)
        emit(repository.deleteGroup(groupId))
    }
    fun getOneGroup(groupId: String) : LiveData<Result<Group>> = liveData(coroutineContext){
        emit(Result.Loading)
        emit(repository.getOneGroup(groupId))
    }
}