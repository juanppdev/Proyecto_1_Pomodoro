package com.mundocode.pomodoro.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mundocode.pomodoro.core.room.dao.HabitsDao
import com.mundocode.pomodoro.core.room.dao.PurchasedDataDao
import com.mundocode.pomodoro.core.room.dao.TaskDao
import com.mundocode.pomodoro.core.room.dao.UserPointsDao
import com.mundocode.pomodoro.domain.repositories.HabitsRepository
import com.mundocode.pomodoro.domain.repositories.PointsRepository
import com.mundocode.pomodoro.domain.repositories.PurchaseRepository
import com.mundocode.pomodoro.domain.repositories.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    fun provideHabitsRepository(habitsDao: HabitsDao, firestore: FirebaseFirestore, auth: FirebaseAuth) =
        HabitsRepository(habitsDao, firestore, auth)

    @Provides
    fun provideTaskRepository(taskDao: TaskDao, firestore: FirebaseFirestore) = TaskRepository(taskDao, firestore)

    @Provides
    fun providePointsRepository(userPointsDao: UserPointsDao) = PointsRepository(userPointsDao)

    @Provides
    fun purchaseRepository(purchasedDataDao: PurchasedDataDao) = PurchaseRepository(
        purchasedDataDao,
    )
}
