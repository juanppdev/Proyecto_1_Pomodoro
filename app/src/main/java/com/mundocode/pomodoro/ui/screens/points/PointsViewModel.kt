package com.mundocode.pomodoro.ui.screens.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

class PointsViewModel @AssistedInject constructor(
    private val pointsRepository: PointsRepository,
    @Assisted private val userId: String,
) : ViewModel() {

    val userPoints: StateFlow<Int> = pointsRepository.getUserPoints(userId).map { it.points }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0,
    )

//            pointsRepository.getUserPoints(userId).collectLatest { userPoints ->
//                this@PointsViewModel.userPoints.update { userPoints.points }
//            }
//        }
//    }

    fun addPoints(userId: String, points: Int) {
        viewModelScope.launch {
            pointsRepository.addPoints(userId, points)
        }
    }

    fun spendPoints(userId: String, points: Int): Boolean {
        var success = false
        viewModelScope.launch {
            success = pointsRepository.spendPoints(userId, points)
        }
        return success
    }

    @AssistedFactory
    interface PointsViewModelFactory {
        fun create(userId: String): PointsViewModel
    }

    companion object {
        fun provideFactory(assistedFactory: PointsViewModelFactory, userId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T = assistedFactory.create(userId) as T
            }
    }
}

// Esta clase es necesaria para exponer la factory a Hilt
@HiltViewModel
class PointsViewModelFactoryProvider @Inject constructor(
    val pointsViewModelFactory: PointsViewModel.PointsViewModelFactory,
) : ViewModel()
