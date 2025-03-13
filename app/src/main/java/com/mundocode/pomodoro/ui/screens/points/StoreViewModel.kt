package com.mundocode.pomodoro.ui.screens.points

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mundocode.pomodoro.domain.repositories.PointsRepository
import com.mundocode.pomodoro.domain.repositories.PurchaseRepository
import com.mundocode.pomodoro.model.local.StoreItem
import com.mundocode.pomodoro.model.room.PurchasedDataEntity
import com.mundocode.pomodoro.ui.theme.ThemePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.toSet

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
                StoreItem(3, "Tema Oscuro", 50, "Tema oscuro para la app"),
                StoreItem(4, "Tema Azul", 100, "Tema azul para la app"),
                StoreItem(5, "Tema Rojo", 150, "Tema rojo para la app"),
                StoreItem(6, "Tema Claro", 0, "Tema Claro para la app"),
            ),
        )

    val purchasedDataEntity: StateFlow<List<PurchasedDataEntity>>
        field = MutableStateFlow<List<PurchasedDataEntity>>(emptyList())

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

    fun loadPurchasedData(userId: String) {
        viewModelScope.launch {
            // 🔹 Obtener los ítems comprados y actualizar el estado
            purchasedRepository.get(userId).collectLatest { items ->
                purchasedDataEntity.update { items }

                val updateData = items.map { it.itemName }.toSet() // 🔹 Filtrar solo nombres válidos
                unlockedThemes.update { updateData + "Tema Claro" } // ✅ Siempre incluir "Tema Claro"
            }
        }
    }

    fun purchaseData(userId: String, item: StoreItem, userPoints: Int): Boolean {
        if (userPoints >= item.price) {
            viewModelScope.launch {
                pointsRepository.spendPoints(userId, item.price)
                val purchasedDataEntity = PurchasedDataEntity(
                    userId = userId,
                    itemName = item.name,
                    itemDescription = item.description,
                    price = item.price,
                )
                purchasedRepository.insert(purchasedDataEntity)
                // ✅ Actualizar `unlockedThemes` inmediatamente en la UI antes de cargar de Room
                unlockedThemes.value = unlockedThemes.value + item.name
                loadPurchasedData(userId)
            }
            return true
        }
        return false
    }
}
