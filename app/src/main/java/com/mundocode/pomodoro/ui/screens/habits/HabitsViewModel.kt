package com.mundocode.pomodoro.ui.screens.habits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.habitsDB.domain.AddTaskUserCase
import com.mundocode.pomodoro.data.habitsDB.domain.DeleteTaskUseCase
import com.mundocode.pomodoro.data.habitsDB.domain.GetTasksUserCase
import com.mundocode.pomodoro.data.habitsDB.domain.UpdateTaskUseCase
import com.mundocode.pomodoro.ui.screens.habits.HabitsUIState.Loading
import com.mundocode.pomodoro.ui.screens.habits.HabitsUIState.Success
import com.mundocode.pomodoro.ui.screens.habits.model.HabitsModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class HabitsViewModel @Inject constructor(
    private val addTaskUserCase: AddTaskUserCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    getTasksUserCase: GetTasksUserCase,
) : ViewModel() {

    private val _showPopup = MutableStateFlow(false)

    val uiState: StateFlow<HabitsUIState> = getTasksUserCase().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onDialogClose() {
        _showDialog.value = false
    }

    fun onTaskCreated(title: String, description: String) {
        _showDialog.value = false

        viewModelScope.launch {
            addTaskUserCase(HabitsModel(title = title, description = description))
        }
    }

    fun onShowDialogSelected() {
        _showDialog.value = true

        fun onItemRemove(taskModel: HabitsModel) {
            viewModelScope.launch {
                deleteTaskUseCase(taskModel)
            }
        }

        fun onTaskUpdated(taskModel: HabitsModel) {
            viewModelScope.launch {
                updateTaskUseCase(taskModel)
            }
        }

        // Mostrar el popup
        fun togglePopup() {
            _showPopup.value = !_showPopup.value
        }
    }
}
