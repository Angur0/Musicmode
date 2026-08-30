# Architecture

## Runtime split

MusicMode keeps normal Android lifecycle work in the application process and uses Shizuku only for shell-privileged operations.

### App process

- `MainActivity`: status and manual controls.
- `MusicModeService`: foreground service and screen-on receiver.
- `BootReceiver`: restores the service after boot when Music Mode was enabled.
- `MusicoletManager`: package detection and launch orchestration.

### Privileged bridge

`ShizukuBridge` executes narrowly scoped shell commands through Shizuku when its binder is available and permission is granted. The current alpha uses the Shizuku remote-process API as the privileged bridge; a UserService can replace it later without changing the higher-level managers.

## Screen-on flow

`ACTION_SCREEN_ON` → 500 ms delay → 3 second cooldown check → `MusicoletManager.launch()` → Shizuku `am start` when available → ordinary launch fallback.

## Design rules

- Never require `SYSTEM_ALERT_WINDOW`.
- Never assume Shizuku survives an Android 10 reboot.
- Fail gracefully when Shizuku or Musicolet is absent.
- Keep debloating and hardware-input experiments out of the initial reliability milestone.
