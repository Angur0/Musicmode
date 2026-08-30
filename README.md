# MusicMode

MusicMode is an Android companion app intended to turn an OPPO A15 (Android 10 / API 29) into a dedicated Musicolet-based music player using Shizuku without root.

## v0.1-alpha scope

- Detect Shizuku binder state and request Shizuku permission.
- Detect Musicolet (`in.krosbits.musicolet`).
- Launch Musicolet normally or through a Shizuku-backed shell command.
- Run a foreground service while Music Mode is enabled.
- Detect `ACTION_SCREEN_ON`, wait 500 ms, and launch Musicolet.
- Apply a 3-second launch cooldown to avoid loops.
- Restore the MusicMode foreground service after `BOOT_COMPLETED` when enabled.
- Gracefully continue in limited mode if Shizuku is not running.

## Important Android 10 limitation

On an unrooted Android 10 device, Shizuku must be started again through ADB after every reboot. MusicMode does not attempt to bypass this limitation.

## ColorOS constraint

This project intentionally does **not** depend on `SYSTEM_ALERT_WINDOW`. Testing on the target OPPO A15 showed that ColorOS blocks shell UID 2000 from `MANAGE_APP_OPS_MODES`, `GRANT_RUNTIME_PERMISSIONS`, and OPPO's protected `OPPO_COMPONENT_SAFE` permission screens. ADB-mode Shizuku inherits shell UID 2000 restrictions.

## Build

Requirements:

- Android Studio / JDK 17
- Android SDK 36
- Android Gradle Plugin 9.3.0
- Shizuku API 13.1.5

Open the repository in Android Studio and sync Gradle. A Gradle wrapper will be added after the first verified local build.

## Initial device test

1. Install and start Shizuku through ADB.
2. Install MusicMode.
3. Open MusicMode and grant Shizuku permission.
4. Verify Musicolet is detected.
5. Tap **Launch Musicolet**.
6. Enable **Music Mode**.
7. Open another app.
8. Turn the screen off and back on.
9. Verify Musicolet is launched after approximately 500 ms.

## Planned phases

After v0.1-alpha is proven reliable on the OPPO A15:

1. improved diagnostics and structured logs;
2. strict/smart/whitelist Music Mode behavior;
3. safe package disable/restore controls;
4. media-key controls;
5. fingerprint/input-event research;
6. optional kiosk-lite mode.

See `docs/` for architecture, ColorOS findings, Shizuku limitations, and testing notes.
