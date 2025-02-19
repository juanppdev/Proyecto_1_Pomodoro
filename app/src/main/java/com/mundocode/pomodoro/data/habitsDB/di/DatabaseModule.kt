package com.mundocode.pomodoro.data.habitsDB.di

import android.content.Context
import androidx.room.Room
import com.mundocode.pomodoro.data.habitsDB.HabitsDao
import com.mundocode.pomodoro.data.habitsDB.HabitsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    fun provideTaskDao(habitsDatabase: HabitsDatabase): HabitsDao = habitsDatabase.taskDao()

    @Provides
    @Singleton
    fun provideTodoDatabase(@ApplicationContext appContext: Context): HabitsDatabase =
        Room.databaseBuilder(appContext, HabitsDatabase::class.java, "HabitsDatabase").build()
}
