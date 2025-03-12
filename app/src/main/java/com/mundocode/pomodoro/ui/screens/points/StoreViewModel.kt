package com.mundocode.pomodoro.ui.screens.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.mundocode.pomodoro.domain.repositories.PointsRepository
import com.mundocode.pomodoro.domain.repositories.PurchaseRepository
import com.mundocode.pomodoro.model.local.StoreItem
import com.mundocode.pomodoro.model.local.StoreTheme
import com.mundocode.pomodoro.model.room.PurchasedItemEntity
import com.mundocode.pomodoro.model.room.PurchasedThemeEntity
import com.mundocode.pomodoro.ui.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class StoreViewModel @Inject constructor(
    private val pointsRepository: PointsRepository,
    private val purchasedRepository: PurchaseRepository,
    private val themePreferences: ThemePreferences,
) : ViewModel() {

    val storeItems: StateFlow<List<StoreItem>>
        field = MutableStateFlow(
            listOf(
                StoreItem(1, "Sonido Especial", 30, "Activa un sonido único al terminar un Pomodoro"),
                StoreItem(2, "Fondo Personalizado", 70, "Elige un fondo exclusivo para la app"),
            ),
        )

    val storeThemes: StateFlow<List<StoreTheme>>
        field = MutableStateFlow(
            listOf(
                StoreTheme(1, "Tema Oscuro", 50, "Tema oscuro para la app"),
                StoreTheme(2, "Tema Azul", 100, "Tema azul para la app"),
                StoreTheme(3, "Tema Rojo", 150, "Tema rojo para la app"),
                StoreTheme(4, "Tema Claro", 0, "Tema Claro para la app"),
            ),
        )

    val purchasedItemsEntity: StateFlow<List<PurchasedItemEntity>>
        field = MutableStateFlow<List<PurchasedItemEntity>>(emptyList())

    val purchasedThemesEntity: StateFlow<List<PurchasedThemeEntity>>
        field = MutableStateFlow<List<PurchasedThemeEntity>>(emptyList())

    val unlockedThemes: StateFlow<Set<String>>
        field = MutableStateFlow(setOf<String>())

    val selectedTheme: StateFlow<String>
        field = MutableStateFlow("Tema Claro")

    init {
        viewModelScope.launch {
            themePreferences.selectedTheme.collect { theme ->
                selectedTheme.value = theme
            }
        }
    }

//    fun loadUserPoints(userId: String) {
//        viewModelScope.launch {
//            pointsRepository.getUserPoints(userId).map { it.points }.stateIn(
//                scope = viewModelScope,
//                started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5_000),
//                initialValue = 0,
//            )
//        }
//    }

    fun loadPurchasedItems(userId: String) {
        viewModelScope.launch {
            // ✅ Verifica si existen temas en la base de datos
            val count = purchasedRepository.countUserPurchasedThemes(userId)
            Timber.tag("StoreViewModel").d("🔍 Temas comprados en la BD: $count") // ✅ Debug

            purchasedRepository.getUserPurchasedThemes(userId).collectLatest { themes ->
                Timber.tag("StoreViewModel").d("📌 Temas cargados desde la BD: $themes") // ✅ Debug
                purchasedThemesEntity.update { themes }
                unlockedThemes.update { themes.map { it.themeName }.toSet() }
            }
        }
    }

    fun loadPurchasedThemes() {
        viewModelScope.launch {
            purchasedRepository.getUserPurchasedThemes(Firebase.auth.currentUser?.uid ?: "").collectLatest { themes ->
                val updatedThemes = themes.map { it.themeName }.toSet()
                unlockedThemes.value = updatedThemes + "Tema Claro" // ✅ Siempre incluir el tema "Claro"
                Timber.tag("StoreViewModel").d("🔓 Temas desbloqueados: $updatedThemes")
            }
        }
    }

    fun purchaseItem(userId: String, item: StoreItem, userPoints: Int): Boolean {
        if (userPoints >= item.price) {
            viewModelScope.launch {
                pointsRepository.spendPoints(userId, item.price)
                val purchasedItemEntity = PurchasedItemEntity(
                    userId = userId,
                    itemName = item.name,
                    itemDescription = item.description,
                    price = item.price,
                )
                purchasedRepository.insert(purchasedItemEntity)
                loadPurchasedItems(userId)
            }
            return true
        }
        return false
    }

    fun purchaseTheme(userId: String, item: StoreTheme, userPoints: Int): Boolean {
        if (userPoints >= item.price) {
            viewModelScope.launch {
                pointsRepository.spendPoints(userId, item.price)
                val purchasedThemeEntity = PurchasedThemeEntity(
                    userId = userId,
                    themeName = item.name,
                    price = item.price,
                    themeDescription = item.description,
                )
                purchasedRepository.insertPurchasedTheme(purchasedThemeEntity)

                // ✅ Actualizar `unlockedThemes` inmediatamente en la UI antes de cargar de Room
                unlockedThemes.value = unlockedThemes.value + item.name

                // ✅ Asegurar que los datos persistan en la base de datos
                loadPurchasedItems(userId)
            }
            return true
        }
        return false
    }
}
