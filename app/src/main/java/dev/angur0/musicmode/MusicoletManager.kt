package dev.angur0.musicmode

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class MusicoletManager(private val context: Context) {
    companion object {
        const val PACKAGE = "in.krosbits.musicolet"
    }

    fun isInstalled(): Boolean = try {
        context.packageManager.getPackageInfo(PACKAGE, 0)
        true
    } catch (_: PackageManager.NameNotFoundException) {
        false
    }

    fun launcherComponent(): ComponentName? {
        val intent = context.packageManager.getLaunchIntentForPackage(PACKAGE) ?: return null
        return intent.component
    }

    fun launch(): LaunchResult {
        if (!isInstalled()) return LaunchResult.NotInstalled
        val component = launcherComponent() ?: return LaunchResult.NoLauncher

        if (ShizukuBridge.isReady()) {
            val result = ShizukuBridge.exec("am start -n ${component.flattenToShortString()}")
            if (result.exitCode == 0) return LaunchResult.Success
        }

        return try {
            val intent = Intent.makeMainActivity(component).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
            LaunchResult.Success
        } catch (t: Throwable) {
            LaunchResult.Failed(t.message ?: t.javaClass.simpleName)
        }
    }
}

sealed interface LaunchResult {
    data object Success : LaunchResult
    data object NotInstalled : LaunchResult
    data object NoLauncher : LaunchResult
    data class Failed(val reason: String) : LaunchResult
}
