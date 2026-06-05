package com.exemplo.copa2026

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.exemplo.copa2026.data.local.AppDatabase
import com.exemplo.copa2026.di.AppContainer
import com.exemplo.copa2026.notification.MatchNotificationScheduler

class CopaApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        AppDatabase.populateIfNeeded(this)
        createNotificationChannel()
        MatchNotificationScheduler.schedule(this, container)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Partidas",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Notificacoes de inicio de partidas"
        }
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    companion object {
        const val CHANNEL_ID = "match_notifications"
    }
}
