package com.mundocode.pomodoro.data.pointsDB.di

import android.content.Context
import androidx.room.Room
import com.mundocode.pomodoro.data.pointsDB.PointsDao
import com.mundocode.pomodoro.data.pointsDB.PointsDatabase
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import com.mundocode.pomodoro.data.storeDB.PurchasedItemsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PointsModule {
    @Provides
    @Singleton
    fun providePointsDatabase(@ApplicationContext context: Context): PointsDatabase = Room.databaseBuilder(
        context.applicationContext,
        PointsDatabase::class.java,
        "points_database",
    ).fallbackToDestructiveMigration().build()

    @Provides
    fun providePointsDao(pointsDatabase: PointsDatabase): PointsDao = pointsDatabase.pointsDao()

    @Provides
    fun providePurchasedItemsDao(pointsDatabase: PointsDatabase): PurchasedItemsDao = pointsDatabase.purchasedItemsDao()

    @Provides
    fun providePointsRepository(pointsDao: PointsDao): PointsRepository = PointsRepository(pointsDao)
}
