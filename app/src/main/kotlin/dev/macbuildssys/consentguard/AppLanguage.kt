package dev.macbuildssys.consentguard

import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.core.app.LocaleManagerCompat

/** Accessibility services and tiles may keep a system-locale context after an app language change. */
internal fun Context.forAppLanguage(): Context {
    val selected = LocaleManagerCompat.getApplicationLocales(this)
    if (selected.isEmpty) return this
    val configuration = Configuration(resources.configuration).apply {
        setLocales(LocaleList.forLanguageTags(selected.toLanguageTags()))
    }
    return createConfigurationContext(configuration)
}
