package dev.macbuildssys.consentguard

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat

internal object ConsentReadyNotification {
    private const val CHANNEL = "consent_review_ready"
    private const val ID = 1

    fun show(context: Context) {
        val notifications = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) return
        if (!notifications.areNotificationsEnabled()) return

        val localized = context.forAppLanguage()
        createChannel(localized)
        val message = localized.getString(R.string.vendor_switches_off_body, localized.getString(R.string.confirm_run))
        val notification = NotificationCompat.Builder(localized, CHANNEL)
            .setSmallIcon(R.drawable.ic_completion_bell)
            .setContentTitle(localized.getString(R.string.vendor_switches_off_title))
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setCategory(NotificationCompat.CATEGORY_STATUS)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setDefaults(NotificationCompat.DEFAULT_SOUND)
            .setTimeoutAfter(5 * 60_000L)
            .build()
        notifications.notify(ID, notification)
    }

    fun clear(context: Context) {
        NotificationManagerCompat.from(context).cancel(ID)
    }

    fun settingsIntent(context: Context): Intent {
        if (Build.VERSION.SDK_INT < 26) return appDetailsIntent(context)
        createChannel(context.forAppLanguage())
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            return Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS)
                .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                .putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL)
        }
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            .putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }

    fun appDetailsIntent(context: Context) =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", context.packageName, null))

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT < 26) return
        val channel = NotificationChannel(
            CHANNEL, context.getString(R.string.completion_alerts), NotificationManager.IMPORTANCE_DEFAULT
        ).apply { setShowBadge(false) }
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }
}
