package com.example.calendarandtasks.data.firebase.grouptask

import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Group

interface GroupTask {
    suspend fun getGroupTask(): Result<List<Group>>
    suspend fun getOneGroup(groupId: String) : Result<Group>
    suspend fun newGroup(group: Group): Result<Boolean>
    suspend fun deleteGroup(groupId : String) : Result<Boolean>
    suspend fun editGroup(groupId: String,group: Group) : Result<Boolean>
}