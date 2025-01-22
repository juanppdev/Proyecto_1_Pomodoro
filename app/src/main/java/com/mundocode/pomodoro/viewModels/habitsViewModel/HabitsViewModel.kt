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

    //Obtención de textos del popup
    fun onAddClick() {

        val currentText1 = _tittle.value
        val currentText2 = _description.value

        println("Titulo: $currentText1")
        println("Descripción: $currentText2")
    }

    //Mostrar el popup
    fun togglePopup() {
        _showPopup.value = !_showPopup.value
    }
}
