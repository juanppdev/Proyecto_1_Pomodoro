package com.mundocode.pomodoro.ui.screens.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PointsViewModel @Inject constructor(private val pointsRepository: PointsRepository) : ViewModel() {

    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints

    fun loadUserPoints(userId: String) {
        viewModelScope.launch {
            pointsRepository.getUserPoints(userId).collectLatest { userPoints ->
                _userPoints.value = userPoints?.points ?: 0
            }
        }
    }

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
}
