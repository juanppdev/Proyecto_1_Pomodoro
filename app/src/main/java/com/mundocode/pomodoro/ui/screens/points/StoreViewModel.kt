package com.mundocode.pomodoro.ui.screens.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mundocode.pomodoro.data.pointsDB.PointsRepository
import com.mundocode.pomodoro.data.storeDB.PurchasedItem
import com.mundocode.pomodoro.data.storeDB.PurchasedItemsDao
import com.mundocode.pomodoro.data.storeDB.PurchasedTheme
import com.mundocode.pomodoro.model.local.StoreItem
import com.mundocode.pomodoro.model.local.StoreTheme
import com.mundocode.pomodoro.ui.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val purchasedItemsDao: PurchasedItemsDao,
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    private val _storeItems = MutableStateFlow(
        listOf(
            StoreItem(1, "Sonido Especial", 30, "Activa un sonido único al terminar un Pomodoro"),
            StoreItem(2, "Fondo Personalizado", 70, "Elige un fondo exclusivo para la app"),
        ),
    )
    val storeItems: StateFlow<List<StoreItem>> = _storeItems.asStateFlow()

    private val _storeThemes = MutableStateFlow(
        listOf(
            StoreTheme(1, "Tema Oscuro", 50, "Tema oscuro para la app"),
            StoreTheme(2, "Tema Azul", 100, "Tema azul para la app"),
            StoreTheme(3, "Tema Rojo", 150, "Tema rojo para la app"),
            StoreTheme(4, "Tema Claro", 0, "Tema Claro para la app"),
        ),
    )
    val storeThemes: StateFlow<List<StoreTheme>> = _storeThemes.asStateFlow()

    private val _userPoints = MutableStateFlow(0)
    val userPoints: StateFlow<Int> = _userPoints.asStateFlow()

    private val _purchasedItems = MutableStateFlow<List<PurchasedItem>>(emptyList())
    val purchasedItems: StateFlow<List<PurchasedItem>> = _purchasedItems.asStateFlow()

    private val _purchasedThemes = MutableStateFlow<List<PurchasedTheme>>(emptyList())
    val purchasedThemes: StateFlow<List<PurchasedTheme>> = _purchasedThemes.asStateFlow()

    private val _unlockedThemes = MutableStateFlow(setOf<String>())
    val unlockedThemes: StateFlow<Set<String>> = _unlockedThemes

    private val _selectedTheme = MutableStateFlow("Tema Claro")
    val selectedTheme: StateFlow<String> = _selectedTheme

    init {
        viewModelScope.launch {
            themePreferences.selectedTheme.collect { theme ->
                _selectedTheme.value = theme
            }
        }
    }

    fun loadUserPoints(userId: String) {
        viewModelScope.launch {
            pointsRepository.getUserPoints(userId).collectLatest { userPoints ->
                _userPoints.value = userPoints?.points ?: 0
            }
        }
    }

    fun loadPurchasedItems(userId: String) {
        viewModelScope.launch {
            val count = purchasedItemsDao.countUserPurchasedThemes(userId) // ✅ Verifica si existen temas en la base de datos
            Timber.tag("StoreViewModel").d("🔍 Temas comprados en la BD: $count") // ✅ Debug

            purchasedItemsDao.getUserPurchasedThemes(userId).collectLatest { themes ->
                Timber.tag("StoreViewModel").d("📌 Temas cargados desde la BD: $themes") // ✅ Debug
                _purchasedThemes.value = themes
                _unlockedThemes.value = themes.map { it.themeName }.toSet()
            }
        }
    }

    fun loadPurchasedThemes() {
        viewModelScope.launch {
            purchasedItemsDao.getUserPurchasedThemes(Firebase.auth.currentUser?.uid ?: "").collectLatest { themes ->
                val updatedThemes = themes.map { it.themeName }.toSet()
                _unlockedThemes.value = updatedThemes + "Tema Claro" // ✅ Siempre incluir el tema "Claro"
                Timber.tag("StoreViewModel").d("🔓 Temas desbloqueados: $updatedThemes")
            }
        }
    }

    fun purchaseItem(userId: String, item: StoreItem): Boolean {
        if (_userPoints.value >= item.price) {
            viewModelScope.launch {
                pointsRepository.spendPoints(userId, item.price)
                val purchasedItem = PurchasedItem(
                    userId = userId,
                    itemName = item.name,
                    itemDescription = item.description,
                    price = item.price,
                )
                purchasedItemsDao.insertPurchasedItem(purchasedItem)
                _userPoints.value -= item.price
                loadPurchasedItems(userId)
            }
            return true
        }
        return false
    }

    fun purchaseTheme(userId: String, item: StoreTheme): Boolean {
        if (_userPoints.value >= item.price) {
            viewModelScope.launch {
                pointsRepository.spendPoints(userId, item.price)
                val purchasedTheme = PurchasedTheme(
                    userId = userId,
                    themeName = item.name,
                    price = item.price,
                    themeDescription = item.description,
                )
                purchasedItemsDao.insertPurchasedTheme(purchasedTheme)

                _userPoints.value -= item.price

                // ✅ Actualizar `unlockedThemes` inmediatamente en la UI antes de cargar de Room
                _unlockedThemes.value = _unlockedThemes.value + item.name

                // ✅ Asegurar que los datos persistan en la base de datos
                loadPurchasedItems(userId)
            }
            return true
        }
        return false
    }
}
