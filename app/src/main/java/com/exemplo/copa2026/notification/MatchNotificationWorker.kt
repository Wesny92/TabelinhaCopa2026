package com.exemplo.copa2026.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.exemplo.copa2026.CopaApplication
import com.exemplo.copa2026.MainActivity
import com.exemplo.copa2026.R

class MatchNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val timeCasa = inputData.getString("timeCasa") ?: "?"
        val emojiCasa = inputData.getString("emojiCasa") ?: ""
        val timeFora = inputData.getString("timeFora") ?: "?"
        val emojiFora = inputData.getString("emojiFora") ?: ""
        val horario = inputData.getString("horario") ?: ""
        val estadio = inputData.getString("estadio") ?: ""

        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return Result.success()
        }

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            applicationContext, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, CopaApplication.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("$emojiCasa $timeCasa x $timeFora $emojiFora")
            .setContentText("Inicia as $horario - $estadio")
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("A partida entre $emojiCasa $timeCasa e $timeFora $emojiFora começa em 5 minutos! $horario - $estadio")
            )
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(id.hashCode(), notification)

        return Result.success()
    }
}
