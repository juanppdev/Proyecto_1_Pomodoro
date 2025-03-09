package com.mundocode.pomodoro.data.pointsDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PointsDao {
    @Query("SELECT * FROM user_points WHERE userId = :userId")
    fun getUserPoints(userId: String): Flow<UserPoints?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdatePoints(userPoints: UserPoints)

    @Query("UPDATE user_points SET points = points + :points WHERE userId = :userId")
    suspend fun addPoints(userId: String, points: Int)

    @Query("UPDATE user_points SET points = points - :points WHERE userId = :userId AND points >= :points")
    suspend fun spendPoints(userId: String, points: Int)
}
