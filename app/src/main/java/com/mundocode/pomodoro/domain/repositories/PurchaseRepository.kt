package com.mundocode.pomodoro.domain.repositories

import com.mundocode.pomodoro.core.room.dao.PurchasedDataDao
import com.mundocode.pomodoro.model.room.PurchasedDataEntity

class PurchaseRepository(private val purchasedDataDao: PurchasedDataDao) {
    // /////////////////////////////////////////////////////////////////////////
    // ITEM
    // /////////////////////////////////////////////////////////////////////////
    suspend fun insert(entity: PurchasedDataEntity) = purchasedDataDao.insert(entity)
    suspend fun countUserPurchasedThemes(userId: String) = purchasedDataDao.countUserPurchasedItem(userId)
    fun get(userId: String) = purchasedDataDao.get(userId)
}
