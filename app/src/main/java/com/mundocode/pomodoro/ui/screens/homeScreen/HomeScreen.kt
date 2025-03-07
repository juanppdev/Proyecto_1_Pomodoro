package com.mundocode.pomodoro.ui.screens.homeScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mundocode.pomodoro.R
import com.mundocode.pomodoro.core.navigation.Destinations
import com.mundocode.pomodoro.ui.components.CustomTopAppBar
import kotlinx.serialization.ExperimentalSerializationApi
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseUser
import com.mundocode.pomodoro.ui.screens.SharedPointsViewModel
import com.mundocode.pomodoro.ui.screens.points.PointsViewModel
import com.mundocode.pomodoro.ui.screens.points.StoreViewModel
import com.mundocode.pomodoro.ui.screens.timer.TimerViewModel
import com.mundocode.pomodoro.ui.theme.ThemeViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Calendar
import com.kiwi.navigationcompose.typed.navigate as kiwiNavigation

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
    pointsViewModel: PointsViewModel = hiltViewModel(),
    sharedPointsViewModel: SharedPointsViewModel = hiltViewModel(),
    timerViewModel: TimerViewModel = hiltViewModel(),
    storeViewModel: StoreViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel(),
) {
    val sessionsData by viewModel.sessionsData.collectAsState()
    val xLabels by viewModel.xLabels.collectAsState()

    var selectedOption by remember { mutableStateOf("Weekly") }
    var expanded by remember { mutableStateOf(false) }
    val user = Firebase.auth.currentUser

    val userPoints by sharedPointsViewModel.userPoints.collectAsState()
    val pomodoroCount by timerViewModel.pomodoroCount.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        pointsViewModel.loadUserPoints(user?.displayName.toString())
        coroutineScope.launch {
            timerViewModel.loadPomodoroStats()
        }
    }

    Scaffold(
        topBar = {
            CustomTopAppBar(
                navController = navController,
                title = "Pomodoro",
                image = user?.photoUrl.toString(),
                navigationIcon = {},
                texto = "Puntos: $userPoints",
                onNavPoints = {
                    navController.kiwiNavigation(Destinations.StoreScreen)
                },
            )
        },
        modifier = Modifier.fillMaxSize(),
    ) { padding ->

        val userId = Firebase.auth.currentUser?.uid ?: ""
        val unlockedThemes by storeViewModel.unlockedThemes.collectAsState()
        val selectedTheme by storeViewModel.selectedTheme.collectAsState()

        LaunchedEffect(Unit) {
            storeViewModel.loadPurchasedItems(userId) // ✅ Cargar temas desbloqueados
        }

        LazyColumn {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(padding),
                ) {
                    val selectedTheme by themeViewModel.selectedTheme.collectAsState()

                    unlockedThemes.forEach { theme ->
                        Button(onClick = { themeViewModel.changeTheme(theme) }) {
                            // ✅ Ahora cambia el tema globalmente
                            Text(theme)
                        }
                    }

                    WelcomeSection(user)
                    FavoritesSection()
                    OptionsSection(navController)
                    StatsSection(
                        selectedOption,
                        onOptionSelected = {
                            selectedOption = it
                            viewModel.loadSessions(it)
                        },
                        sessionsData,
                        xLabels,
                    )
                }
            }
        }
    }
}

@Composable
fun WelcomeSection(user: FirebaseUser?) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Bienvenid@, ${user?.displayName ?: "Usuario"}",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )
        Text(
            text = "¿Qué quieres hacer hoy?",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
fun FavoritesSection() {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Tus favoritos",
            fontSize = 24.sp,
            modifier = Modifier.padding(8.dp),
            color = MaterialTheme.colorScheme.onSurface,
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            FavouritesButtons(0xFFFF4E21)
            FavouritesButtons(0xFF6366F1)
            FavouritesButtons(0xFF900300)
        }
    }
}

@OptIn(ExperimentalSerializationApi::class)
@Composable
fun OptionsSection(navController: NavController) {
    Column(modifier = Modifier.padding(8.dp)) {
        OptionButtons(
            color = MaterialTheme.colorScheme.primary,
            textButton = "Empezar\nPomodoro",
            icon = R.drawable.timer_icon,
            descriptionIcon = "botón de Empezar Pomodoro",
            onClick = { navController.kiwiNavigation(Destinations.SetupSessionScreen) },
        )

        OptionButtons(
            color = MaterialTheme.colorScheme.secondary,
            textButton = "Ver\nHábitos",
            icon = R.drawable.habit_icon,
            descriptionIcon = "botón Ver Hábitos",
            onClick = { navController.kiwiNavigation(Destinations.HabitsScreen) },
        )

        OptionButtons(
            color = MaterialTheme.colorScheme.tertiary,
            textButton = "Ver\nTareas",
            icon = R.drawable.checklist_icon,
            descriptionIcon = "botón Ver Tareas",
            onClick = { navController.kiwiNavigation(Destinations.TaskScreen) },
        )
    }
}

@Composable
fun StatsSection(
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    sessionsData: Map<String, Float>,
    xLabels: List<String>,
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        // Dropdown para seleccionar la vista
        Box {
            OutlinedButton(
                modifier = Modifier.padding(10.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.surface),
                onClick = { expanded = true },
            ) {
                Row {
                    Text(selectedOption, color = MaterialTheme.colorScheme.onSurface)
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown")
                }
            }
            DropdownMenu(
                modifier = Modifier.padding(10.dp),
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                listOf("Daily", "Weekly", "Monthly").forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            if (selectedOption != option) {
                                onOptionSelected(option)
                            }
                        },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar el gráfico según la opción seleccionada
        when (selectedOption) {
            "Daily" -> DailyChart(sessionsData, xLabels)
            "Weekly" -> BarChartS(sessionsData, xLabels)
            "Monthly" -> MonthlyCalendar(sessionsData)
        }
    }
}

@Composable
fun DailyChart(sessionsData: Map<String, Float>, xLabels: List<String>) {
    if (sessionsData.isEmpty()) {
        Text(
            "⚠️ No hay datos de hoy",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.inverseSurface,
        )
        return
    }
    val dataEntries = sessionsData.entries.mapIndexed { index, entry ->
        BarEntry(index.toFloat(), entry.value) // ✅ Mostrar datos en minutos
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                setTouchEnabled(true)

                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f

                xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setLabelCount(xLabels.size, true)

                data = BarData(
                    BarDataSet(dataEntries, "Sesión de hoy").apply {
                        colors = listOf(Color.Blue.toArgb(), Color.Red.toArgb())
                        valueFormatter = IntegerValueFormatter()
                    },
                )
                invalidate()
            }
        },
    )
}

@Composable
fun BarChartS(sessionsData: Map<String, Float>, xLabels: List<String>) {
    Timber.tag("BarChartS").d("📊 Datos recibidos en el gráfico: $sessionsData")

    if (sessionsData.isEmpty()) {
        Text("⚠️ No hay sesiones registradas", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        return
    }

    val dataEntries = sessionsData.entries.mapIndexed { index, entry ->
        BarEntry(index.toFloat(), entry.value)
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                legend.isEnabled = true
                setTouchEnabled(true)

                axisLeft.granularity = 1f
                axisLeft.axisMinimum = 0f
                axisRight.granularity = 1f
                axisRight.axisMinimum = 0f
                xAxis.valueFormatter = IndexAxisValueFormatter(xLabels)
                xAxis.granularity = 1f
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawGridLines(false)
                xAxis.setLabelCount(xLabels.size, true)

                data = BarData(
                    BarDataSet(dataEntries, "Sesiones").apply {
                        colors = listOf(Color.Blue.toArgb(), Color.Red.toArgb())
                        valueFormatter = IntegerValueFormatter()
                    },
                )
                invalidate()
            }
        },
    )
}

/** ✅ Formateador para evitar decimales en los valores del gráfico */
class IntegerValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return value.toInt().toString() // ✅ Muestra solo números enteros
    }
}

@Composable
fun MonthlyCalendar(sessionsData: Map<String, Float>) {
    val today = Calendar.getInstance()
    val currentDay = today.get(Calendar.DAY_OF_MONTH)
    val daysInMonth = today.getActualMaximum(Calendar.DAY_OF_MONTH)

    // 📌 Obtener el primer día del mes (Ej: Si es Miércoles, será 4)
    val firstDayOfMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1)
    }.get(Calendar.DAY_OF_WEEK) - 1 // 🔹 Restar 1 para que el Lunes sea `0`

    // 📌 Lista que agrega espacios vacíos antes del primer día del mes
    val dayList = List(firstDayOfMonth) { null } + (1..daysInMonth).toList() // 🔹 Agregar lista de días comenzando desde 1

    var selectedDay by remember { mutableStateOf<Int?>(null) }
    var selectedSessionTime by remember { mutableStateOf<Float?>(null) }
    var showPopup by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        modifier = Modifier.height(200.dp),
        columns = GridCells.Fixed(7),
    ) {
        items(dayList.size) { index ->
            val day = dayList[index] // 🔹 Puede ser `null` para los espacios vacíos
            val sessionTime = day?.let { sessionsData[it.toString()] } ?: 0f

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(40.dp)
                    .background(
                        when {
                            day == null -> Color.Transparent // 🔹 No mostrar espacios vacíos
                            sessionTime > 0 -> Color.Green
                            day == currentDay -> Color.Yellow
                            else -> Color.LightGray
                        },
                        shape = CircleShape,
                    )
                    .clickable(enabled = day != null) {
                        // 🔹 Evita clics en espacios vacíos
                        Timber.tag("Calendar").d("📅 Día $day clickeado, sesión: $sessionTime minutos")
                        if (sessionTime > 0) {
                            selectedDay = day
                            selectedSessionTime = sessionTime
                            showPopup = true
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                if (day != null) { // 🔹 Solo mostrar texto si es un día válido
                    Text(
                        text = day.toString(),
                        color = Color.Black,
                        fontWeight = if (day == currentDay) FontWeight.Bold else FontWeight.Normal,
                    )
                }
            }
        }
    }

    if (showPopup) {
        ShowSessionPopup(
            day = selectedDay!!,
            sessionTime = selectedSessionTime!!,
            onDismiss = { showPopup = false },
        )
    }
}

@Composable
fun ShowSessionPopup(day: Int, sessionTime: Float, onDismiss: () -> Unit) {
    Timber.tag("PopUp").d("📅 Mostrando PopUp para el día $day, tiempo: $sessionTime minutos") // ✅ Verificar si se activa

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sesiones del día $day", color = MaterialTheme.colorScheme.inverseSurface) },
        text = {
            Text("Tiempo total: ${sessionTime.toInt()} minutos", color = MaterialTheme.colorScheme.inverseSurface)
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Aceptar")
            }
        },
    )
}

@Composable
fun FavouritesButtons(color: Long) {
    Button(
        onClick = {},
        modifier = Modifier.size(120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(Color(color)),
    ) { }
}

@Composable
fun OptionButtons(color: Color, textButton: String, icon: Int, descriptionIcon: String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(color),
        modifier = Modifier.padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .size(80.dp)
                .clip(RoundedCornerShape(24.dp))
                .padding(
                    start = 72.dp,
                    end = 72.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = textButton,
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimary,
                lineHeight = 24.sp,
            )
            Icon(
                painterResource(id = icon),
                contentDescription = descriptionIcon,
                modifier = Modifier
                    .size(58.dp),
            )
        }
    }
}
