package com.mundocode.pomodoro.di

import android.content.Context
import com.mundocode.pomodoro.core.room.PomodoroDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): PomodoroDatabase =
        PomodoroDatabase.getInstance(context)

    @Provides
    fun provideTaskDao(database: PomodoroDatabase) = database.taskDao()

    @Provides
    fun provideHabitsDao(database: PomodoroDatabase) = database.habitsDao()

    @Provides
    fun provideSessionDao(database: PomodoroDatabase) = database.sessionDao()

    @Provides
    fun provideUserPointsDao(database: PomodoroDatabase) = database.userPointsDao()

    @Provides
    fun providePurchaseItemsDao(database: PomodoroDatabase) = database.purchasedItemsDao()

    @Provides
    fun providePurchaseThemeDao(database: PomodoroDatabase) = database.purchasedThemeDao()
}
