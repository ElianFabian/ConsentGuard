package dev.macbuildssys.consentguard

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

/* The apps ConsentGuard leaves alone. Apps are always shown and typed by the
   name on the home screen, never by package name. Adding or removing an app
   is saved at once. */
class RulesActivity : Activity() {

    private lateinit var nameInput: AutoCompleteTextView
    private lateinit var listBox: LinearLayout

    /* Home screen name of every installed app, as shown to the person. Read
       on a background thread, since asking every app for its name can take
       long enough on a full phone to freeze the screen. Null until read. */
    @Volatile private var installedNames: List<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rules)

        nameInput = findViewById(R.id.appNameInput)
        listBox = findViewById(R.id.skippedAppsList)

        findViewById<Button>(R.id.addAppButton).setOnClickListener { addTypedApp() }
        findViewById<Button>(R.id.showProtectedButton).setOnClickListener { showProtectedApps() }
        nameInput.setOnEditorActionListener { _, _, _ ->
            addTypedApp()
            true
        }

        showSkippedApps()
        Thread {
            val names = loadInstalledNames()
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                installedNames = names
                nameInput.setAdapter(ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, names))
            }
        }.start()
    }

    private fun launcherApps() =
        packageManager.queryIntentActivities(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), 0)
            .filter { it.activityInfo.packageName != packageName }

    private fun loadInstalledNames(): List<String> =
        launcherApps().map { it.loadLabel(packageManager).toString() }
            .toSet()
            .sortedBy { SafeZoneWords.fold(it) }

    private fun savedNames(): MutableList<String> {
        val raw = getSharedPreferences(ConsentBlockerService.PREFS_NAME, MODE_PRIVATE)
            .getString(ConsentBlockerService.KEY_SKIPPED_APPS, "") ?: ""
        return raw.split('\n').map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()
    }

    private fun saveNames(names: List<String>) {
        getSharedPreferences(ConsentBlockerService.PREFS_NAME, MODE_PRIVATE).edit()
            .putString(ConsentBlockerService.KEY_SKIPPED_APPS, names.joinToString("\n"))
            .apply()
    }

    /* Only a name that matches an installed app is accepted, so a typing
       mistake can never end up on the list looking as if it worked. */
    private fun addTypedApp() {
        val typed = nameInput.text.toString().trim()
        if (typed.isEmpty()) return
        val installed = installedNames
        if (installed == null) {
            Toast.makeText(this, R.string.apps_still_loading, Toast.LENGTH_SHORT).show()
            return
        }
        val folded = SafeZoneWords.fold(typed)
        val match = installed.firstOrNull { SafeZoneWords.fold(it) == folded }
        if (match == null) {
            Toast.makeText(this, getString(R.string.app_not_found, typed), Toast.LENGTH_LONG).show()
            return
        }
        val names = savedNames()
        if (names.none { SafeZoneWords.fold(it) == folded }) {
            names.add(match)
            saveNames(names)
        }
        nameInput.setText("")
        (getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager)
            ?.hideSoftInputFromWindow(nameInput.windowToken, 0)
        showSkippedApps()
        Toast.makeText(this, getString(R.string.app_saved, match), Toast.LENGTH_SHORT).show()
    }

    private fun showSkippedApps() {
        listBox.removeAllViews()
        for (name in savedNames()) {
            val row = LinearLayout(this)
            row.orientation = LinearLayout.HORIZONTAL
            row.gravity = Gravity.CENTER_VERTICAL

            val label = TextView(this)
            label.text = name
            label.textSize = 16f
            row.addView(label, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))

            val remove = Button(this)
            remove.setText(R.string.remove)
            remove.contentDescription = getString(R.string.remove_app, name)
            remove.minWidth = (104 * resources.displayMetrics.density).toInt()
            remove.setOnClickListener {
                val names = savedNames()
                names.removeAll { SafeZoneWords.fold(it) == SafeZoneWords.fold(name) }
                saveNames(names)
                showSkippedApps()
            }
            row.addView(remove)
            listBox.addView(row)
        }
    }

    /* Lets the person check, on their own phone, which installed apps
       ConsentGuard will never touch: the built in ones and the ones they
       added themselves. It uses the same checks the service uses, so the
       list is the real behaviour and not a promise. */
    private fun showProtectedApps() {
        Thread {
            val protectedNames = HashSet<String>()
            val addedByPerson = savedNames().map { SafeZoneWords.fold(it) }.toSet()
            for (info in launcherApps()) {
                val label = info.loadLabel(packageManager).toString()
                if (SafeZoneWords.fold(label) in addedByPerson) {
                    protectedNames.add(getString(R.string.added_by_you, label))
                } else if (SafeZoneWords.isSafeZone(this, info.activityInfo.packageName)) {
                    protectedNames.add(label)
                }
            }
            val sorted = protectedNames.sortedBy { SafeZoneWords.fold(it) }
            runOnUiThread {
                if (isFinishing || isDestroyed) return@runOnUiThread
                val message = if (sorted.isEmpty()) getString(R.string.no_protected_apps) else sorted.joinToString("\n")
                AlertDialog.Builder(this)
                    .setTitle(R.string.protected_title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show()
            }
        }.start()
    }
}
