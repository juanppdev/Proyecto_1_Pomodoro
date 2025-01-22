package com.mundocode.pomodoro.viewModels.habitsViewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class HabitsViewModel : ViewModel() {

    private val _showPopup = MutableStateFlow(false)

    private val _tittle = MutableStateFlow("")
    private val _description = MutableStateFlow("")

    val showPopup = _showPopup.asStateFlow()

    val tittle = _tittle.asStateFlow()
    val description = _description.asStateFlow()

    fun updateTittle(newText: String) {
        _tittle.value = newText
    }

    fun updateDescription(newText: String) {
        _description.value = newText
    }

    fun onAddClick() {
        // Aquí puedes manejar la lógica para guardar o procesar los textos
        val currentText1 = _tittle.value
        val currentText2 = _description.value

        // Lógica para guardar los textos, por ejemplo:
        println("Titulo: $currentText1")
        println("Descripción: $currentText2")
    }

    fun togglePopup() {
        _showPopup.value = !_showPopup.value
    }
}
