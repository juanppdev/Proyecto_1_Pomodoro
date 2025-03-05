package com.mundocode.pomodoro.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth

@HiltViewModel
class SharedPointsViewModel @Inject constructor(private val pointsRepository: PointsRepository) : ViewModel() {

    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints

    fun spendPoints(points: Int) {
        _userPoints.value -= points
    }

    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    init {
        loadUserPoints()
    }

    fun loadUserPoints() {
        viewModelScope.launch {
            pointsRepository.getUserPoints(userId).collectLatest { userPoints ->
                _userPoints.value = userPoints?.points ?: 0
            }
        }
    }

    fun addPoints(points: Int) {
        viewModelScope.launch {
            pointsRepository.addPoints(userId, points)
            loadUserPoints() // Recargar puntos después de actualizarlos
        }
    }
}
