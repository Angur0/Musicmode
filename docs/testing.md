# Testing

## v0.1 acceptance test

The alpha milestone is successful when this works repeatedly on the OPPO A15:

1. Start Shizuku.
2. Open MusicMode and grant permission.
3. Enable Music Mode.
4. Open a different app.
5. Turn the screen off.
6. Turn the screen on.
7. Musicolet becomes foreground after the configured delay.

Run the cycle at least 20 times and verify there are no launch loops or service crashes.

## Matrix

| Test | Shizuku | Expected |
| --- | --- | --- |
| Manual launch | On | Musicolet opens |
| Screen-on launch | On | Musicolet opens after ~500 ms |
| Shizuku stopped | Off | Graceful fallback/no crash |
| Shizuku restarted | On again | Privileged launch becomes available again |
| Device reboot | Off initially | MusicMode service restores if enabled |
| Musicolet absent | Either | UI reports not found; no crash |
| Rapid screen toggles | On | Cooldown prevents launch storm |

## ColorOS longevity tests

Test after 5 minutes, 30 minutes, 2 hours, and overnight with the screen off. Repeat with battery saver on/off and after clearing recent apps.
