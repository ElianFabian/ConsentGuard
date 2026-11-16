package dev.macbuildssys.consentguard

import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

/* A quick settings tile that pauses ConsentGuard for a few minutes and
   resumes it on a second tap. For the odd site that breaks when its banner is
   rejected, and where the person would rather press Accept themselves. It
   only changes one time stamp in the app's own settings, which the service
   reads before every scan. It keeps nothing else and shows nothing on
   screen. While the service itself is switched off in the accessibility
   settings the tile shows as unavailable, since there is nothing to pause. */
class ConsentTileService : TileService() {

    override fun onStartListening() {
        super.onStartListening()
        refresh()
    }

    override fun onClick() {
        super.onClick()
        if (!ConsentBlockerService.isEnabled(this)) {
            refresh()
            return
        }
        val prefs = getSharedPreferences(ConsentBlockerService.PREFS_NAME, MODE_PRIVATE)
        val now = System.currentTimeMillis()
        val until = if (isPaused()) 0L else now + ConsentBlockerService.PAUSE_MS
        prefs.edit().putLong(ConsentBlockerService.KEY_PAUSED_UNTIL, until).apply()
        refresh()
    }

    private fun isPaused(): Boolean {
        val prefs = getSharedPreferences(ConsentBlockerService.PREFS_NAME, MODE_PRIVATE)
        return prefs.getLong(ConsentBlockerService.KEY_PAUSED_UNTIL, 0L) > System.currentTimeMillis()
    }

    private fun refresh() {
        val tile = qsTile ?: return
        when {
            !ConsentBlockerService.isEnabled(this) -> {
                tile.state = Tile.STATE_UNAVAILABLE
                tile.label = getString(R.string.tile_label)
            }
            isPaused() -> {
                tile.state = Tile.STATE_INACTIVE
                tile.label = getString(R.string.tile_label_paused)
            }
            else -> {
                tile.state = Tile.STATE_ACTIVE
                tile.label = getString(R.string.tile_label)
            }
        }
        tile.updateTile()
    }
}
