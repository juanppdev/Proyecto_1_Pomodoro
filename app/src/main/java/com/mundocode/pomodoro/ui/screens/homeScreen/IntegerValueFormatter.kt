package com.mundocode.pomodoro.ui.screens.homeScreen

import com.github.mikephil.charting.formatter.ValueFormatter

/** ✅ Formateador para evitar decimales en los valores del gráfico */
class IntegerValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString() // ✅ Muestra solo números enteros
    }
}
