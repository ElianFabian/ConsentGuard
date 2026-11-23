package dev.macbuildssys.consentguard

import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.color.DynamicColors
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputEditText
import java.text.Collator

class RulesActivity : AppCompatActivity() {
    private data class InstalledApp(val packageName: String, val name: String, val info: ResolveInfo, val protected: Boolean)

    private val preferences by lazy { getSharedPreferences(ConsentBlockerService.PREFS_NAME, MODE_PRIVATE) }
    private lateinit var search: TextInputEditText
    private lateinit var skippedOnly: MaterialSwitch
    private lateinit var empty: TextView
    private var installed: List<InstalledApp>? = null
    private var visibleApps = emptyList<InstalledApp>()
    private var skippedPackages = emptySet<String>()
    private var legacyNames = emptySet<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)
        findViewById<MaterialToolbar>(R.id.appsToolbar).setNavigationOnClickListener { finish() }
        search = findViewById(R.id.appsSearch)
        skippedOnly = findViewById(R.id.appsSkippedOnly)
        empty = findViewById(R.id.appsEmpty)
        val list = findViewById<ListView>(R.id.appsList)
        list.emptyView = empty
        list.adapter = adapter
        search.doAfterTextChanged { refresh() }
        skippedOnly.setOnCheckedChangeListener { _, _ -> refresh() }
        refresh()

        Thread {
            val query = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
            val collator = Collator.getInstance(resources.configuration.locales[0])
            val apps = packageManager.queryIntentActivities(query, 0)
                .distinctBy { it.activityInfo.packageName }
                .filter { it.activityInfo.packageName != packageName }
                .map { info ->
                    val id = info.activityInfo.packageName
                    val label = info.loadLabel(packageManager).toString().ifBlank { id }
                    InstalledApp(id, label, info, SafeZoneWords.isSafeZone(this, id))
                }
                .sortedWith { a, b -> collator.compare(a.name, b.name) }

            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                installed = apps
                // Resolve older name-based entries once, including apps that share the same label.
                val names = preferences.getString(ConsentBlockerService.KEY_SKIPPED_APPS, "").orEmpty()
                    .lineSequence().filter(String::isNotBlank).toList()
                val matches = apps.groupBy { SafeZoneWords.fold(it.name) }
                val migrated = names.flatMap { matches[SafeZoneWords.fold(it)].orEmpty() }.map(InstalledApp::packageName)
                skippedPackages = preferences.getStringSet(ConsentBlockerService.KEY_SKIPPED_PACKAGES, emptySet())
                    .orEmpty() + migrated
                val unmatched = names.filter { SafeZoneWords.fold(it) !in matches }
                legacyNames = unmatched.map(SafeZoneWords::fold).toSet()
                if (migrated.isNotEmpty()) preferences.edit()
                    .putStringSet(ConsentBlockerService.KEY_SKIPPED_PACKAGES, skippedPackages)
                    .putString(ConsentBlockerService.KEY_SKIPPED_APPS, unmatched.joinToString("\n"))
                    .apply()
                refresh()
            }
        }.start()
    }

    private fun isSkipped(app: InstalledApp): Boolean =
        app.protected || app.packageName in skippedPackages || SafeZoneWords.fold(app.name) in legacyNames

    private fun refresh() {
        val query = SafeZoneWords.fold(search.text?.toString().orEmpty())
        visibleApps = installed.orEmpty().filter {
            (!skippedOnly.isChecked || isSkipped(it)) &&
                (query.isEmpty() || query in SafeZoneWords.fold(it.name) || query in SafeZoneWords.fold(it.packageName))
        }
        empty.setText(if (installed == null) R.string.apps_loading else R.string.apps_no_matches)
        adapter.notifyDataSetChanged()
    }

    private val adapter = object : BaseAdapter() {
        override fun getCount() = visibleApps.size
        override fun getItem(position: Int) = visibleApps[position]
        override fun getItemId(position: Int) = position.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val row = convertView ?: layoutInflater.inflate(R.layout.item_skip_app, parent, false)
            val app = getItem(position)
            val checked = isSkipped(app)
            row.findViewById<ImageView>(R.id.skipAppIcon).setImageDrawable(app.info.loadIcon(packageManager))
            row.findViewById<TextView>(R.id.skipAppName).text = app.name
            row.findViewById<TextView>(R.id.skipAppDetail).apply {
                text = if (app.protected) getString(R.string.app_always_skipped) else app.packageName
            }
            row.findViewById<MaterialSwitch>(R.id.skipAppToggle).apply {
                setOnCheckedChangeListener(null)
                contentDescription = getString(R.string.skip_named_app, app.name)
                isChecked = checked
                isEnabled = !app.protected
                setOnCheckedChangeListener { _, skip ->
                    skippedPackages = if (skip) skippedPackages + app.packageName else skippedPackages - app.packageName
                    preferences.edit().putStringSet(ConsentBlockerService.KEY_SKIPPED_PACKAGES, skippedPackages).apply()
                    refresh()
                }
                row.setOnClickListener { if (isEnabled) toggle() }
            }
            return row
        }
    }
}
