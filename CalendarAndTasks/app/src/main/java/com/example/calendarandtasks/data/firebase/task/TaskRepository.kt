package com.example.calendarandtasks.data.firebase.task

import com.google.firebase.auth.FirebaseAuth
import com.example.calendarandtasks.data.firebase.Result
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.example.calendarandtasks.data.model.Task
import com.orhanobut.logger.Logger
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TaskRepository : Tasks {

    private val db = Firebase.firestore
    private val uidUser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
    private val formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val calendar: Calendar = Calendar.getInstance()
    val date = formatDate.format(calendar.time)

    override suspend fun getTask(groupId: String): Result<List<Task>> =
        suspendCoroutine{ cont ->
            db.collection("users/" + uidUser+"/groupTask/" + groupId +"/tasks" )
                .get()
                .addOnSuccessListener {
                    try {
                        cont.resume(Result.Success(it.toObjects()))
                    }catch (e : Exception) {
                        cont.resume(Result.Error(e))
                    }
                }.addOnFailureListener {
                    cont.resume(Result.Error(it))
                }
        }



    override suspend fun newTask(groupId: String, task: Task): Result<Boolean> =
        suspendCoroutine{ cont ->
            db.collection("users/" +uidUser+"/groupTask/" +groupId +"/tasks" ).add(
                task
            ).addOnSuccessListener {
                cont.resume(Result.Success(true))
            }
                .addOnFailureListener{
                    cont.resume(Result.Error(it))
                }
        }

    override suspend fun deleteTask(groupId: String,taskId: String) : Result<Boolean> =
        suspendCoroutine { cont->
            db.collection("users/" +uidUser+"/groupTask/" +groupId +"/tasks" ).document(taskId).delete()
                .addOnSuccessListener {
                    cont.resume(Result.Success(true))
                }
                .addOnFailureListener{
                    cont.resume(Result.Error(it))
                }
        }

    override suspend fun getTaskToDay(): Result<List<Task>> =
        suspendCoroutine { cont->
            readTaskToday(object :CallBackList{
                override fun onCallBackList(value: List<Task>) {
                    try{
                        cont.resume(Result.Success(value))
                    }catch (e:Exception){
                     //   cont.resume(Result.Error(e))
                    }
                }

            })

        }

    //    override suspend fun getTaskToDay(): Result<List<Task>> =
//        suspendCoroutine { cont->
//            db.collection("users/" +uidUser+ "/groupTask").get()
//                .addOnSuccessListener { gr->
//                    var listTask = ArrayList<Task>()
//                    for (doc in gr){
//                        db.collection("users/" +uidUser+"/groupTask/" +doc.id+"/tasks").whereEqualTo("dateStart",date).get()
//                            .addOnSuccessListener {
//                                ts->
//                                if(!ts.isEmpty){
//                                    listTask.addAll(ts.toObjects())
//
//                                }
//                            }
//                            .addOnFailureListener{
//                                cont.resume(Result.Error(it))
//                            }
//                    }
//                    try{
//                        cont.resume(Result.Success(listTask))
//                    }catch (e:Exception){
//                        cont.resume(Result.Error(e))
//                    }
//                }
//                .addOnFailureListener{
//                    cont.resume(Result.Error(it))
//                }
//        }
    fun readGrIdToday(callback : GetGrId){
        db.collection("users/" +uidUser+ "/groupTask").get()
            .addOnSuccessListener { gr->

//                var listTask = ArrayList<Task>()
                var listId = ArrayList<String>()
                for (doc in gr){
//                    db.collection("users/" +uidUser+"/groupTask/" +doc.id+"/tasks").whereEqualTo("dateStart",date).get()
//                        .addOnSuccessListener {
//                                ts-> listTask.addAll(ts.toObjects())
//                            callback.onCallBack(listTask)
//                        }
                    listId.add(doc.id)

                }
                callback.callGrId(listId)
            }
            .addOnFailureListener{
            }
    }
    fun readTaskToday(callback : CallBackList){
        readGrIdToday(object :GetGrId{
            override fun callGrId(value: List<String>) {
                val listTask = ArrayList<Task>()
                value.forEach {
                    db.collection("users/" +uidUser+"/groupTask/" +it+"/tasks").whereEqualTo("dateStart",date).get()
                        .addOnSuccessListener {
                            listTask.addAll(it.toObjects())
                            callback.onCallBackList(listTask)
                        }
                }
            }

        })

    }



}
interface CallBackList{
    fun onCallBackList(value: List<Task>)
}
interface GetGrId{
    fun callGrId(value: List<String>)
}