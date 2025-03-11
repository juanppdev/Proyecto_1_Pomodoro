package com.mundocode.pomodoro.domain.repositories

import com.mundocode.pomodoro.core.room.dao.PurchasedItemsDao
import com.mundocode.pomodoro.core.room.dao.PurchasedThemeDao
import com.mundocode.pomodoro.model.room.PurchasedItemEntity
import com.mundocode.pomodoro.model.room.PurchasedThemeEntity

class PurchaseRepository(
    private val purchasedItemsDao: PurchasedItemsDao,
    private val purchasedThemeItemsDao: PurchasedThemeDao,
) {
    // /////////////////////////////////////////////////////////////////////////
    // ITEM
    // /////////////////////////////////////////////////////////////////////////
    suspend fun insert(entity: PurchasedItemEntity) = purchasedItemsDao.insert(entity)

    // /////////////////////////////////////////////////////////////////////////
    // THEME
    // /////////////////////////////////////////////////////////////////////////
    suspend fun countUserPurchasedThemes(userId: String) = purchasedThemeItemsDao.countUserPurchasedThemes(userId)

    fun getUserPurchasedThemes(userId: String) = purchasedThemeItemsDao.getUserPurchasedThemes(userId)

    suspend fun insertPurchasedTheme(entity: PurchasedThemeEntity) = purchasedThemeItemsDao.insert(entity)
}
