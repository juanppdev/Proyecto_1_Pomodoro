package com.mundocode.pomodoro.ui.screens.habits

import android.util.Log
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
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HabitsViewModel @Inject constructor(private val habitsRepository: HabitsRepository) : ViewModel() {

    val uiState: StateFlow<HabitsUIState> = habitsRepository.getHabits().map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    val showDialog: StateFlow<Boolean>
        field = MutableStateFlow<Boolean>(false)

    val searchQuery: StateFlow<String>
        field = MutableStateFlow("")

    val filterList: StateFlow<List<Habits>>
        field = MutableStateFlow<List<Habits>>(emptyList())

    init {
        viewModelScope.launch {
            searchQuery
//                .debounce(500) // Espera 500ms después del último cambio antes de consultar Firestore
                .collectLatest { query ->
//                    Timber.tag("TEST").d("query: $query")
                    Log.d("TEST", "query: $query")
                    if (query.isEmpty()) {
                        filterList.update { emptyList() }
                    } else {
                        habitsRepository.getHabitsByTitle(query).collectLatest {
                            filterList.update { it }
                        }
                    }
                }
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.update { query } // Se actualiza el StateFlow, lo que dispara la búsqueda con debounce
    }

    fun onDialogClose() {
        showDialog.update { false }
    }

    fun onTaskCreated(title: String, description: String) {
//        showDialog.update { false }

        Timber.tag("TEST").d("title: $title, description: $description")

        viewModelScope.launch {
            habitsRepository.addHabit(title = title, description = description)
        }
    }

    fun onShowDialogSelected() {
        showDialog.update { true }
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
