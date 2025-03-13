package com.mundocode.pomodoro.ui.screens.homeScreen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.mundocode.pomodoro.ui.theme.PomodoroTheme

@Composable
fun DailyChart(sessionsData: Map<String, Float>, timeData: Map<String, Float>, xLabels: List<String>) {
    if (sessionsData.isEmpty() || timeData.isEmpty()) {
        Text(
            text = "⚠️ No hay datos de hoy",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier.padding(16.dp),
        )
        return
    }

    val legendColor = MaterialTheme.colorScheme.inverseSurface.toArgb()
    val axisTextColor = MaterialTheme.colorScheme.inverseSurface.toArgb()
    var valueTextColor = MaterialTheme.colorScheme.onSurface.toArgb()

    val sessionsBarColor = Color(0xFF81D4FA).toArgb() // Azul claro
    val timeBarColor = Color(0xFFD1C4E9).toArgb() // Morado claro

    val sessionEntries = sessionsData.entries.mapIndexed { index, entry ->
        BarEntry(index.toFloat(), entry.value)
    }

    val timeEntries = timeData.entries.mapIndexed { index, entry ->
        BarEntry(index.toFloat(), entry.value)
    }

    key(sessionsData, timeData) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(8.dp),
            factory = { context ->
                BarChart(context).apply {
                    description.isEnabled = false
                    setTouchEnabled(true)
                    setDrawGridBackground(false)

                    legend.apply {
                        isEnabled = true
                        textColor = legendColor
                        form = Legend.LegendForm.SQUARE
                    }

                    axisLeft.apply {
                        axisMinimum = 0f
                        axisMaximum = maxOf(
                            sessionsData.values.maxOrNull() ?: 0f,
                            timeData.values.maxOrNull() ?: 0f,
                        ) + 1f
                        textColor = axisTextColor
                        setDrawGridLines(false)
                    }

                    axisRight.isEnabled = false

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(xLabels)
                        position = XAxis.XAxisPosition.BOTTOM
                        setDrawGridLines(false)
                        textColor = axisTextColor
                        setLabelCount(xLabels.size, true)
                        granularity = 1f
                        axisMinimum = -0.5f // 🔹 Evita desajustes en la alineación
                    }

                    // 🔹 **Dataset de Sessions**
                    val sessionsDataSet = BarDataSet(sessionEntries, "Sessions").apply {
                        color = sessionsBarColor
                        valueTextColor = valueTextColor
                        valueFormatter = IntegerValueFormatter()
                    }

                    // 🔹 **Dataset de Total Time**
                    val timeDataSet = BarDataSet(timeEntries, "Total Time (minutes)").apply {
                        color = timeBarColor
                        valueTextColor = valueTextColor
                        valueFormatter = IntegerValueFormatter()
                    }

                    Log.d("DailyChart", "🟣 TotalTimeDataSet: $timeDataSet")

                    val barData = BarData(sessionsDataSet, timeDataSet).apply {
                        barWidth = 0.1f // 🔹 Ajusta el ancho de las barras
                    }

                    data = barData

                    // ✅ **Agrupar barras correctamente**
                    barData.groupBars(0f, 0.2f, 0.05f)

                    notifyDataSetChanged()
                    invalidate()
                }
            },
        )
    }
}

@Preview
@Composable
fun DailyChartPreview() {
    PomodoroTheme {
        DailyChart(
            sessionsData = mapOf("Monday" to 5f, "Tuesday" to 3f, "Wednesday" to 8f),
            timeData = mapOf("Monday" to 20f, "Tuesday" to 15f, "Wednesday" to 25f),
            xLabels = listOf("Monday", "Tuesday", "Wednesday"),
        )
    }
}
