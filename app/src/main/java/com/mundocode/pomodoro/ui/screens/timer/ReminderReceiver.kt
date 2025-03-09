package com.mundocode.pomodoro.ui.screens.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.mundocode.pomodoro.MainActivity
import com.mundocode.pomodoro.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        showReminderNotification(context)
    }

    private fun showReminderNotification(context: Context) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Crear el canal de notificación en Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "pomodoro_reminder_channel",
                "Recordatorio de Pomodoro",
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }

        // Intent para abrir la app cuando se toque la notificación
        val openIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("fromReminder", true) // ✅ Detectar si se abrió desde la notificación
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        // Construir la notificación
        val notification = NotificationCompat.Builder(context, "pomodoro_reminder_channel")
            .setSmallIcon(R.drawable.timer)
            .setContentTitle("¡Hora de un Pomodoro! 🍅")
            .setContentText("No has iniciado un Pomodoro en un tiempo. ¡Empieza uno ahora!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification) // ✅ Mostrar la notificación
    }
}
