# CookieRejecter privacy

This page is written so you can check each claim yourself, not just believe it.

## What it does

CookieRejecter is an Android accessibility service. It reads on-screen text to find consent forms. After you tap the floating button, it can interact with the form and briefly inspect pixels in recognized Google forms to verify switch states. It discards each screenshot after the check.

## What it never does

**It cannot send anything off your phone.**
The app has no INTERNET permission. Android enforces this. Look at `AndroidManifest.xml` (the permission is removed on purpose) or run `kotlinc -script tools/check_no_network.kts`.

**Banking and payment apps are not monitored.**
CookieRejecter checks whether to skip an app before reading its screen. **Apps to skip** marks apps recognized by the built-in banking, payment, email, and chat rules. See `isSafeZone` in `ConsentBlockerService.kt` and `SafeZoneWords.kt`.

**The activity log stays in memory.**
It includes app names, consent actions, and switch changes. It clears when the process exits or when you clear it in the app. Debug builds can also write diagnostics to Android’s system log.

**Settings are stored locally.**
The app saves your skipped apps, the quick-settings pause time, and the selected language.

## How to be sure it has no internet access

On Android an app can only use the network if its manifest declares the INTERNET permission. This is a normal permission: Android grants it automatically at install and never shows a prompt for it, so users cannot see it being given. That is why the manifest is the place to look. An app that does not declare it is refused every connection by the system. So the question comes down to one thing you can look at. Three ways, from easy to thorough:

1. **On your phone.** Open the app’s data usage in Android Settings and look for CookieRejecter.

2. **With adb.** Run `adb shell dumpsys package dev.cookierejecter` and check the requested permissions for `android.permission.INTERNET`.

3. **From the APK itself.** Run `aapt dump permissions app-debug.apk` and check the output for `android.permission.INTERNET`.

For the source, run `kotlinc -script tools/check_no_network.kts`. It fails if the manifest asks for a network permission, or if the code uses networking classes or opens links or share screens that could pass data to another app.

What this does not cover: a phone with a rooted system or a modified Android could break these rules for any app. That is true for every app on that phone.


## Reporting a problem

Open an issue on GitHub with the app name.
