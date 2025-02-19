package com.mundocode.pomodoro.data.sessionDb

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
abstract class SessionDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
