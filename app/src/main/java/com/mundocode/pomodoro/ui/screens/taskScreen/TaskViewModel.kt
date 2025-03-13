package com.mundocode.pomodoro.ui.screens.taskScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.domain.repositories.TaskRepository
import com.mundocode.pomodoro.model.room.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    val tasks = repository.getAllTasks().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun addTask(title: String, category: String) {
        viewModelScope.launch {
            repository.insertTask(TaskEntity(title = title, category = category))
        }
    }

    fun toggleTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task.copy(completed = !task.completed))
        }
    }

    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}
