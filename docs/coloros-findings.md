# ColorOS Findings

Target device: OPPO A15, Android 10 / API 29.

Observed during setup:

- `android.hardware.ram.normal` is present; the device is not Android Go low-RAM mode.
- Automation declares `android.permission.SYSTEM_ALERT_WINDOW`, but its AppOp remained `ignore`.
- `adb shell appops set ... SYSTEM_ALERT_WINDOW allow` failed because shell UID 2000 lacks `MANAGE_APP_OPS_MODES`.
- `adb shell pm grant ... SYSTEM_ALERT_WINDOW` failed because shell UID 2000 lacks `GRANT_RUNTIME_PERMISSIONS`.
- OPPO SafeCenter floating-window and permission activities exist but require `oppo.permission.OPPO_COMPONENT_SAFE`.

Conclusion: MusicMode must not depend on overlay permission or OPPO's protected permission activities. Shizuku started through ADB runs with shell-level privilege and therefore should be expected to inherit these same OEM restrictions.
