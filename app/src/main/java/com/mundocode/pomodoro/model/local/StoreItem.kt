package com.mundocode.pomodoro.model.local

import kotlinx.serialization.Serializable

@Serializable
data class StoreItem(val id: Int, val name: String, val price: Int, val description: String)

@Serializable
data class StoreTheme(val id: Int, val name: String, val price: Int, val description: String)
