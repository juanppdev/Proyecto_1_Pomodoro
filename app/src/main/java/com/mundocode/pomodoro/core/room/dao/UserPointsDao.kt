package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.USER_POINTS_TABLE_NAME
import com.mundocode.pomodoro.model.room.UserPointsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPointsDao {
    @Query("SELECT * FROM $USER_POINTS_TABLE_NAME WHERE userId = :userId")
    fun get(userId: String): Flow<UserPointsEntity?>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(userPointsEntity: UserPointsEntity)

    @Query("UPDATE $USER_POINTS_TABLE_NAME SET points = points + :points WHERE userId = :userId")
    suspend fun addPoints(userId: String, points: Int)

    @Query("UPDATE $USER_POINTS_TABLE_NAME SET points = points - :points WHERE userId = :userId AND points >= :points")
    suspend fun spendPoints(userId: String, points: Int)
}
