# Fingerprint Research

Fingerprint remapping is intentionally excluded from v0.1-alpha.

Future investigation order:

1. Run `adb shell getevent -lp` and identify whether the rear fingerprint sensor is exposed as an input device.
2. Run `adb shell getevent` while tapping/swiping the sensor and record events.
3. Check Android fingerprint gesture API availability on the OPPO A15.
4. If raw events are visible to shell, prototype read-only event monitoring through a Shizuku-backed service.
5. Map supported gestures to media key events only after reliable detection is proven.

Do not attempt to bypass secure authentication or the lock screen.
