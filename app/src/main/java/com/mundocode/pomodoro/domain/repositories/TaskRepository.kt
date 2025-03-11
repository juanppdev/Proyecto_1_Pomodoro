package com.mundocode.pomodoro.domain.repositories

import com.google.firebase.firestore.FirebaseFirestore
import com.mundocode.pomodoro.core.room.dao.TaskDao
import com.mundocode.pomodoro.model.room.TaskEntity
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class TaskRepository @Inject constructor(private val taskDao: TaskDao, private val firestore: FirebaseFirestore) {

    fun getAllTasks(): Flow<List<TaskEntity>> = taskDao.get()

    suspend fun insertTask(task: TaskEntity) {
        taskDao.insert(task)
        firestore.collection("tasks").document(task.title).set(task)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.update(task)
        firestore.collection("tasks").document(task.title).set(task)
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.delete(task)
        firestore.collection("tasks").document(task.title).delete()
    }
}
