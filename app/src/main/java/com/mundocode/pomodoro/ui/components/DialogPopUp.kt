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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun DialogPopUp(
    show: Boolean,
    onDismiss: () -> Unit,
    onTaskAdded: (String, String) -> Unit,
    title: String,
    description: String,
    onValueChangeTitle: (String) -> Unit,
    onValueChangeDescription: (String) -> Unit,
) {
    if (show) {
        Dialog(onDismissRequest = { onDismiss() }) {
            Surface(shape = RoundedCornerShape(8.dp), shadowElevation = 8.dp) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Nuevo hábito")

                    TextField(
                        value = title,
                        onValueChange = { onValueChangeTitle(it) },
                        singleLine = true,
                        maxLines = 1,
                        label = { Text("Título") },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    TextField(
                        value = description,
                        onValueChange = { onValueChangeDescription(it) },
                        maxLines = 20,
                        label = { Text("Descripción") },
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(8.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Button(
                                onClick = {
                                    onTaskAdded(title, description)
                                    title
                                    description
                                    onDismiss()
                                },
                                modifier = Modifier.padding(end = 8.dp),
                            ) {
                                Text("Guardar")
                            }
                        }
                        Column(
                            modifier = Modifier.weight(1f).padding(8.dp),
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
