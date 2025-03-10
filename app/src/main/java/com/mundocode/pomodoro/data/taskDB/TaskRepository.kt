package com.mundocode.pomodoro.data.taskDB

import com.google.firebase.firestore.FirebaseFirestore
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository @Inject constructor(private val taskDao: TaskDao, private val firestore: FirebaseFirestore) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.getAllTasks()

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
        firestore.collection("tasks").document(task.title).set(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
        firestore.collection("tasks").document(task.title).set(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
        firestore.collection("tasks").document(task.title).delete()
    }
}
