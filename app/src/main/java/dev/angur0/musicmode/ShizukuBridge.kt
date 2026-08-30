package dev.angur0.musicmode

import android.content.pm.PackageManager
import rikka.shizuku.Shizuku

object ShizukuBridge {
    data class CommandResult(val exitCode: Int, val stdout: String, val stderr: String)

    fun isReady(): Boolean = try {
        Shizuku.pingBinder() && Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED
    } catch (_: Throwable) {
        false
    }

    @Suppress("DEPRECATION")
    fun exec(command: String): CommandResult {
        if (!isReady()) return CommandResult(-1, "", "Shizuku unavailable or permission not granted")
        return try {
            val process = Shizuku.newProcess(arrayOf("sh", "-c", command), null, null)
            val stdout = process.inputStream.bufferedReader().readText()
            val stderr = process.errorStream.bufferedReader().readText()
            val code = process.waitFor()
            CommandResult(code, stdout, stderr)
        } catch (t: Throwable) {
            CommandResult(-1, "", t.stackTraceToString())
        }
    }
}
