package dev.angur0.musicmode

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val enabled = context.getSharedPreferences("music_mode", Context.MODE_PRIVATE)
            .getBoolean("enabled", false)
        if (enabled) {
            context.startForegroundService(Intent(context, MusicModeService::class.java))
        }
    }
}
