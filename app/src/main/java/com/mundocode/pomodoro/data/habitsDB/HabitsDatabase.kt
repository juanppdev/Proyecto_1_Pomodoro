package com.mundocode.pomodoro.data.habitsDB

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [HabitsEntity::class], version = 1)
abstract class HabitsDatabase : RoomDatabase() {

    abstract fun taskDao(): HabitsDao
}
