package com.mundocode.pomodoro.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mundocode.pomodoro.ui.theme.PomodoroTheme

@Composable
fun DialogPopUp(show: Boolean, onDismiss: () -> Unit = {}, onTaskAdded: (String, String) -> Unit = { _, _ -> }) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }

    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(shape = RoundedCornerShape(8.dp), shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nuevo hábito")

                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                        maxLines = 1,
                        label = { Text("Title", color = MaterialTheme.colorScheme.inverseSurface) },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = description,
                        onValueChange = { description = it },
                        maxLines = 20,
                        label = { Text("Description", color = MaterialTheme.colorScheme.inverseSurface) },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Button(
                                onClick = {
                                    onTaskAdded(title, description)
                                    title = ""
                                    description = ""
                                    onDismiss()
                                },
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text("Guardar")
                            }
                        }
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Button(onClick = { onDismiss() }) {
                                Text("Cancelar")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogPopUpPreview() {
    PomodoroTheme {
        DialogPopUp(show = true)
    }
}
