package com.mundocode.pomodoro.data.pointsDB

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class PointsRepository(private val pointsDao: PointsDao) {

    fun getUserPoints(userId: String): Flow<UserPoints?> = pointsDao.getUserPoints(userId)

    suspend fun addPoints(userId: String, points: Int) {
        val currentPoints = pointsDao.getUserPoints(userId).firstOrNull()
        if (currentPoints == null) {
            pointsDao.insertOrUpdatePoints(UserPoints(userId = userId, points = points)) // Inserta un nuevo usuario
        } else {
            pointsDao.addPoints(userId, points) // Actualiza si ya existe
        }
    }

    suspend fun spendPoints(userId: String, points: Int): Boolean {
        val currentPoints = pointsDao.getUserPoints(userId).firstOrNull() // Obtiene el valor actual
        return if (currentPoints != null && currentPoints.points >= points) {
            pointsDao.spendPoints(userId, points)
            true
        } else {
            false
        }
    }
}
