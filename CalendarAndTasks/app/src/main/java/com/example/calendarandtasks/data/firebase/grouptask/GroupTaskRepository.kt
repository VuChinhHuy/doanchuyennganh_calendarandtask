package com.example.calendarandtasks.data.firebase.grouptask

import com.example.calendarandtasks.data.firebase.Result
import com.example.calendarandtasks.data.model.Group
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GroupTaskRepository : GroupTask {
    private val db = Firebase.firestore
    private val uidUser = FirebaseAuth.getInstance().currentUser!!.uid.toString()
    override suspend fun getGroupTask(): Result<List<Group>> =
        suspendCoroutine{ cont ->
            db.collection("users/" +uidUser+"/groupTask" )
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

    override suspend fun getOneGroup(groupId: String): Result<Group> =
        suspendCoroutine { cont->
            db.collection("users/" +uidUser+"/groupTask").document(groupId).get()
                .addOnSuccessListener {
                    val group  = it.toObject<Group>()
                    cont.resume(Result.Success(group!!))
                }
                .addOnFailureListener{
                    cont.resume(Result.Error(it))
                }
        }

    override suspend fun newGroup(group: Group): Result<Boolean> =
        suspendCoroutine { cont ->
            db.collection("users/" +uidUser+"/groupTask").add(
                group
            ).addOnSuccessListener {
                cont.resume(Result.Success(true))
            }
                .addOnFailureListener{
                    cont.resume(Result.Error(it))
                }
    }

    override suspend fun deleteGroup(groupId: String): Result<Boolean> =
        suspendCoroutine { cont->
            db.collection("users/" +uidUser+"/groupTask").document(groupId).delete()
                .addOnSuccessListener {
                    cont.resume(Result.Success(true))
                }
                .addOnFailureListener{
                    cont.resume(Result.Error(it))
                }
        }



    override suspend fun editGroup(groupId: String, group: Group): Result<Boolean> =
        suspendCoroutine { cont ->
            db.collection("users/" + uidUser + "/groupTask").document(groupId).set(group)
                .addOnSuccessListener {
                    cont.resume(Result.Success(true))
                }
                .addOnFailureListener {
                    cont.resume(Result.Error(it))
                }
        }

}