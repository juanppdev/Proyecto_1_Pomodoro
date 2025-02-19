package com.mundocode.pomodoro.data.sessionDb

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext appContext: Context): SessionDatabase =
        Room.databaseBuilder(
            appContext,
            SessionDatabase::class.java,
            "session_database",
        ).build()

    @Provides
    fun provideSessionDao(database: SessionDatabase): SessionDao = database.sessionDao()
}
