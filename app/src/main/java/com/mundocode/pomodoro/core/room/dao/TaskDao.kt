package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.TASK_TABLE_NAME
import com.mundocode.pomodoro.model.room.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Query("SELECT * FROM $TASK_TABLE_NAME ORDER BY completed ASC")
    fun get(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)
}
