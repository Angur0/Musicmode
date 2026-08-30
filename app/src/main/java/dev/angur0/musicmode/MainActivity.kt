package dev.angur0.musicmode

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import rikka.shizuku.Shizuku

class MainActivity : Activity() {
    private lateinit var status: TextView

    private val binderListener = Shizuku.OnBinderReceivedListener { refreshStatus() }
    private val deadListener = Shizuku.OnBinderDeadListener { refreshStatus() }
    private val permissionListener = Shizuku.OnRequestPermissionResultListener { _, _ -> refreshStatus() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Shizuku.addBinderReceivedListenerSticky(binderListener)
        Shizuku.addBinderDeadListener(deadListener)
        Shizuku.addRequestPermissionResultListener(permissionListener)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(48, 64, 48, 48)
        }

        status = TextView(this)
        layout.addView(status)

        layout.addView(Button(this).apply {
            text = "Grant Shizuku permission"
            setOnClickListener {
                if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    Shizuku.requestPermission(1001)
                }
            }
        })

        layout.addView(Button(this).apply {
            text = "Launch Musicolet"
            setOnClickListener { MusicoletManager(this@MainActivity).launch() }
        })

        layout.addView(Button(this).apply {
            text = "Enable Music Mode"
            setOnClickListener {
                getSharedPreferences("music_mode", MODE_PRIVATE).edit().putBoolean("enabled", true).apply()
                startForegroundService(Intent(this@MainActivity, MusicModeService::class.java))
                refreshStatus()
            }
        })

        layout.addView(Button(this).apply {
            text = "Disable Music Mode"
            setOnClickListener {
                getSharedPreferences("music_mode", MODE_PRIVATE).edit().putBoolean("enabled", false).apply()
                stopService(Intent(this@MainActivity, MusicModeService::class.java))
                refreshStatus()
            }
        })

        layout.addView(Button(this).apply {
            text = "Open Shizuku"
            setOnClickListener {
                packageManager.getLaunchIntentForPackage("moe.shizuku.privileged.api")?.let(::startActivity)
                    ?: startActivity(Intent(Settings.ACTION_APPLICATION_SETTINGS))
            }
        })

        setContentView(layout)
        refreshStatus()
    }

    private fun refreshStatus() {
        runOnUiThread {
            val binder = Shizuku.pingBinder()
            val permission = binder && Shizuku.checkSelfPermission() == android.content.pm.PackageManager.PERMISSION_GRANTED
            val installed = MusicoletManager(this).isInstalled()
            val enabled = getSharedPreferences("music_mode", MODE_PRIVATE).getBoolean("enabled", false)
            status.text = "MusicMode v0.1-alpha\n\nShizuku: ${if (binder) "Running" else "Not running"}\nPermission: ${if (permission) "Granted" else "Not granted"}\nMusicolet: ${if (installed) "Installed" else "Not found"}\nMusic Mode: ${if (enabled) "Enabled" else "Disabled"}"
        }
    }

    override fun onDestroy() {
        Shizuku.removeBinderReceivedListener(binderListener)
        Shizuku.removeBinderDeadListener(deadListener)
        Shizuku.removeRequestPermissionResultListener(permissionListener)
        super.onDestroy()
    }
}
