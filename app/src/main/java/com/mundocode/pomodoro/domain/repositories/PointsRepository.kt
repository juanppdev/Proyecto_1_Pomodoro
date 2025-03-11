package com.mundocode.pomodoro.domain.repositories

import com.mundocode.pomodoro.core.room.dao.UserPointsDao
import com.mundocode.pomodoro.model.room.UserPointsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull

class PointsRepository(private val userPointsDao: UserPointsDao) {

    fun getUserPoints(userId: String): Flow<UserPointsEntity> = userPointsDao.get(userId).filterNotNull()

    suspend fun addPoints(userId: String, points: Int) {
        val currentPoints = userPointsDao.get(userId).firstOrNull()
        if (currentPoints == null) {
            userPointsDao.insert(UserPointsEntity(userId = userId, points = points)) // Inserta un nuevo usuario
        } else {
            userPointsDao.addPoints(userId, points) // Actualiza si ya existe
        }
    }

    suspend fun spendPoints(userId: String, points: Int): Boolean {
        val currentPoints = userPointsDao.get(userId).firstOrNull() // Obtiene el valor actual
        return if (currentPoints != null && currentPoints.points >= points) {
            userPointsDao.spendPoints(userId, points)
            true
        } else {
            false
        }
    }
}
