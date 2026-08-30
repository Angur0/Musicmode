# Shizuku Limitations

## Android 10 startup

Without root, Shizuku does not automatically survive a full reboot on Android 10. After reboot, MusicMode may start its own service, but Shizuku-backed actions remain unavailable until Shizuku is started again through ADB.

## Privilege level

ADB-mode Shizuku operates with shell-level privileges. It is not equivalent to root and cannot bypass OEM restrictions that explicitly deny shell UID 2000.

## MusicMode behavior when unavailable

MusicMode should:

1. keep the app usable;
2. report Shizuku as not running/not granted;
3. attempt an ordinary Musicolet launch when appropriate;
4. automatically use Shizuku again when its binder becomes available.
