package dev.angur0.musicmode

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock

class MusicModeService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private var lastLaunchAt = 0L

    private val screenReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action != Intent.ACTION_SCREEN_ON) return
            if (!prefs().getBoolean("enabled", false)) return
            handler.postDelayed({ launchMusicoletWithCooldown() }, 500L)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createChannel()
        registerReceiver(screenReceiver, IntentFilter(Intent.ACTION_SCREEN_ON))
        val openIntent = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = android.app.Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("MusicMode active")
            .setContentText("Screen-on Musicolet launcher enabled")
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setContentIntent(openIntent)
            .build()
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun launchMusicoletWithCooldown() {
        val now = SystemClock.elapsedRealtime()
        if (now - lastLaunchAt < 3000L) return
        lastLaunchAt = now
        MusicoletManager(this).launch()
    }

    private fun prefs() = getSharedPreferences("music_mode", MODE_PRIVATE)

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java).createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "MusicMode service", NotificationManager.IMPORTANCE_LOW)
            )
        }
    }

    override fun onDestroy() {
        unregisterReceiver(screenReceiver)
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val CHANNEL_ID = "music_mode_service"
        private const val NOTIFICATION_ID = 1001
    }
}
