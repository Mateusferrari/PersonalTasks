package com.mateus.personaltasks.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.mateus.personaltasks.model.Task

class FirebaseTaskRepository {
    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun addTask(task: Task, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("tasks")
            .add(task.toMap(userId))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun updateTask(task: Task, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("tasks").document(task.id!!)
            .set(task.toMap(userId))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun deleteTask(task: Task, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("deleted_tasks").document(task.id!!)
            .set(task.toMap(userId))
            .addOnSuccessListener {
                db.collection("tasks").document(task.id!!).delete()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    fun getTasks(onComplete: (List<Task>) -> Unit) {
        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { it.toTask() }
                onComplete(tasks)
            }
    }

    fun getDeletedTasks(onComplete: (List<Task>) -> Unit) {
        db.collection("deleted_tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val tasks = result.map { it.toTask() }
                onComplete(tasks)
            }
    }

    fun restoreTask(task: Task, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        db.collection("tasks").document(task.id!!)
            .set(task.toMap(userId))
            .addOnSuccessListener {
                db.collection("deleted_tasks").document(task.id!!).delete()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { onError(it) }
            }
            .addOnFailureListener { onError(it) }
    }

    private fun Task.toMap(userId: String) = mapOf(
        "title" to title,
        "description" to description,
        "deadline" to deadline,
        "isDone" to isDone,
        "userId" to userId
    )

    private fun QueryDocumentSnapshot.toTask(): Task {
        return Task(
            id = id,
            title = getString("title") ?: "",
            description = getString("description") ?: "",
            deadline = getString("deadline") ?: "",
            isDone = getBoolean("isDone") ?: false
        )
    }
}
