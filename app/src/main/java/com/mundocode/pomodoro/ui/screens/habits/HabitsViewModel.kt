package com.mundocode.pomodoro.ui.screens.habits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.domain.repositories.HabitsRepository
import com.mundocode.pomodoro.model.local.Habits
import com.mundocode.pomodoro.ui.screens.habits.HabitsUIState.Loading
import com.mundocode.pomodoro.ui.screens.habits.HabitsUIState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HabitsViewModel @Inject constructor(private val habitsRepository: HabitsRepository) : ViewModel() {

    val uiState: StateFlow<HabitsUIState> = habitsRepository.getHabits().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    init {
        habitsRepository.syncFromFirestore(viewModelScope, "")
    }

    val showDialog: LiveData<Boolean>
        field = MutableLiveData<Boolean>()

    val searchQuery: StateFlow<String>
        field = MutableStateFlow("")

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500) // Espera 500ms después del último cambio antes de consultar Firestore
                .collectLatest { query ->
                    habitsRepository.syncFromFirestore(viewModelScope, query)
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query // Se actualiza el StateFlow, lo que dispara la búsqueda con debounce
    }

    fun onDialogClose() {
        showDialog.value = false
    }

    fun onTaskCreated(title: String, description: String) {
        showDialog.value = false

        viewModelScope.launch {
            habitsRepository.addHabit(Habits(title = title, description = description))
        }
    }

    fun onShowDialogSelected() {
        showDialog.value = true
    }

    fun onItemRemove(taskModel: Habits) {
        viewModelScope.launch {
            habitsRepository.deleteHabit(taskModel)
        }
    }

    fun onTaskUpdated(taskModel: Habits) {
        viewModelScope.launch {
            habitsRepository.updateHabit(taskModel)
        }
    }
}
