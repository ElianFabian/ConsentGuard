package dev.macbuildssys.consentguard

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var statusText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        ConsentBlockerService.forgetOldHistory(this)

        findViewById<Button>(R.id.openSettingsButton).setOnClickListener {
            /* A few device makers ship without the accessibility settings
               screen as a separate page. The general settings are the next
               best place, and a crash is never right. */
            try {
                startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
            } catch (e: ActivityNotFoundException) {
                try {
                    startActivity(Intent(Settings.ACTION_SETTINGS))
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(this, R.string.settings_missing, Toast.LENGTH_LONG).show()
                }
            }
        }

        findViewById<Button>(R.id.openRulesButton).setOnClickListener {
            startActivity(Intent(this, RulesActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        statusText.setText(
            if (ConsentBlockerService.isEnabled(this)) R.string.status_enabled else R.string.status_disabled
        )
    }
}
