package com.mundocode.pomodoro.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mundocode.pomodoro.core.room.PomodoroDatabase.Companion.HABITS_TABLE_NAME
import com.mundocode.pomodoro.model.room.HabitsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {

    @Query("SELECT * FROM $HABITS_TABLE_NAME")
    fun getHabits(): Flow<List<HabitsEntity>> // ✅ Asegurar que devuelve un Flow

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE title LIKE :title")
    fun getHabitsByTitle(title: String): Flow<List<HabitsEntity>>

    @Query("SELECT * FROM $HABITS_TABLE_NAME WHERE id = :id LIMIT 1")
    suspend fun getHabitById(id: Int): HabitsEntity?

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE) // ✅ Evita duplicados reemplazando registros existentes
    suspend fun insert(habit: HabitsEntity)

    @Update
    suspend fun update(habit: HabitsEntity)

    @Delete
    suspend fun delete(habit: HabitsEntity)

    @Query("DELETE FROM $HABITS_TABLE_NAME")
    suspend fun clearHabits()
}
