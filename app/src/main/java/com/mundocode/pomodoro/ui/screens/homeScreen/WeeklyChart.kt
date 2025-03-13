package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun WeeklyChart(sessionsData: Map<String, Float>, xLabels: List<String>) {
    if (sessionsData.isEmpty()) {
        Text(
            text = "⚠️ No hay sesiones registradas",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inverseSurface,
            modifier = Modifier.padding(16.dp),
        )
        return
    }

    val legendColor = MaterialTheme.colorScheme.inverseSurface.toArgb()
    val axisTextColor = MaterialTheme.colorScheme.inverseSurface.toArgb()
    var valueTextColor = MaterialTheme.colorScheme.inverseSurface.toArgb()
    val barColor = MaterialTheme.colorScheme.secondary.toArgb()

    val dataEntries = remember(sessionsData) {
        sessionsData.entries.mapIndexed { index, entry -> BarEntry(index.toFloat(), entry.value) }
    }

    key(sessionsData) {
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp),
            factory = { context ->
                BarChart(context).apply {
                    description.isEnabled = false
                    legend.apply {
                        isEnabled = true
                        textColor = legendColor
                    }
                    setTouchEnabled(true)

                    axisLeft.apply {
                        axisMinimum = 0f
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
                    }

                    data = BarData(
                        BarDataSet(dataEntries, "Sesiones").apply {
                            color = barColor
                            valueTextColor = valueTextColor
                            valueFormatter = IntegerValueFormatter()
                        },
                    )
                    invalidate()
                }
            },
        )
    }
}
