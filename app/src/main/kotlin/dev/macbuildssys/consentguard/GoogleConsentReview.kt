package dev.macbuildssys.consentguard

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.Path
import android.graphics.Rect
import android.os.Handler
import android.os.Build
import android.util.Log
import android.view.Display
import android.view.accessibility.AccessibilityNodeInfo

/** Inspects both panels in the Google form before requesting submission. */
@TargetApi(30)
internal class GoogleConsentReview(
    private val service: AccessibilityService,
    private val handler: Handler,
    private val packageName: String,
    private val activeRoot: () -> AccessibilityNodeInfo?,
    private val canTouch: (Int, Int) -> Boolean,
    private val readyToConfirm: (() -> Unit) -> Unit,
    private val nextScan: (Long) -> Unit,
    private val finish: (Int) -> Unit
) {
    private class Progress(val panel: GoogleConsentPanel) {
        val controls = panel.controls
        val bodyChildCount = panel.body.childCount
        val checked = mutableSetOf<Int>()
        var vicinity = 0
        var atTop = panel.top
        var lastViewport: List<Pair<Int, Rect>>? = null
        var repeatedViewport = 0
        var pendingSwitch: Int? = null
        var pendingRechecks = 0
        var unstableReads = 0
        var unknownSwitch: Int? = null
        var unknownReads = 0
    }

    private val completed = mutableSetOf<GoogleConsentPanel.Kind>()
    private var progress: Progress? = null
    private var waiting = false

    fun step(root: AccessibilityNodeInfo) {
        if (waiting) return
        var panel = GoogleConsentPanel.read(root, readControls = false) ?: return stop(R.string.log_google_panel_changed)
        val previous = progress?.panel
        if (previous != null && !previous.sameForm(panel)) return stop(R.string.log_google_panel_changed)
        if (previous?.kind != panel.kind) {
            panel = GoogleConsentPanel.read(root, readControls = true) ?: return stop(R.string.log_google_panel_changed)
            if (panel.controls.isEmpty() || panel.controls.any { it.name.isNullOrBlank() }) {
                return stop(R.string.log_google_inventory_unknown)
            }
            progress = Progress(panel).apply {
                val first = panel.visible.firstOrNull()?.index
                vicinity = controls.indexOfFirst { it.index == first }.coerceAtLeast(0)
            }
        }
        val state = checkNotNull(progress)
        if (state.bodyChildCount != panel.body.childCount) {
            return stop(R.string.log_google_inventory_unknown)
        }
        val start = (state.vicinity - 8).coerceAtLeast(0)
        val end = (state.vicinity + 12).coerceAtMost(state.controls.lastIndex)
        var visible = (start..end).mapNotNull { index ->
            val control = state.controls[index]
            val row = panel.body.getChild(control.index) ?: return@mapNotNull null
            val node = row.getChild(row.childCount - 1) ?: return@mapNotNull null
            if (node.className?.toString() != "android.widget.ToggleButton") return@mapNotNull null
            val bounds = Rect().also(node::getBoundsInScreen)
            if (bounds.isEmpty || !panel.viewport.contains(bounds)) return@mapNotNull null
            index to control.copy(node = node, bounds = bounds)
        }
        if (visible.isEmpty() && state.repeatedViewport >= 1) {
            val refreshed = GoogleConsentPanel.read(root, readControls = true)
                ?: return stop(R.string.log_google_panel_changed)
            visible = refreshed.visible.mapNotNull { control ->
                val index = state.controls.indexOfFirst { it.index == control.index }
                if (index < 0) null else index to control
            }
        }
        visible.firstOrNull()?.let { state.vicinity = it.first }
        val displayed = visible.map { it.second }

        if (!state.atTop) {
            if (!panel.top) return scroll(panel, state, displayed, forward = false)
            state.atTop = true
            state.lastViewport = null
            state.repeatedViewport = 0
        }
        val remaining = displayed.filter { it.index !in state.checked }
        if (remaining.isEmpty()) return advance(panel, state, displayed)
        inspect(panel, state, remaining, displayed)
    }

    private fun inspect(
        panel: GoogleConsentPanel,
        state: Progress,
        visible: List<GoogleConsentPanel.Control>,
        displayed: List<GoogleConsentPanel.Control>
    ) {
        waiting = true
        service.takeScreenshot(Display.DEFAULT_DISPLAY, { handler.post(it) },
            object : AccessibilityService.TakeScreenshotCallback {
                override fun onSuccess(screenshot: AccessibilityService.ScreenshotResult) {
                    screenshot.hardwareBuffer.use { buffer ->
                        val root = activeRoot() ?: return
                        val current = GoogleConsentPanel.read(root, readControls = false)
                        if (current == null || !current.samePanel(panel) || current.viewport != panel.viewport) {
                            return stop(R.string.log_google_panel_changed)
                        }
                        val hardware = Bitmap.wrapHardwareBuffer(buffer, screenshot.colorSpace)
                            ?: return stop(R.string.log_google_capture_failed)
                        val image = try {
                            hardware.copy(Bitmap.Config.ARGB_8888, false)
                                ?: return stop(R.string.log_google_capture_failed)
                        } finally {
                            hardware.recycle()
                        }
                        try {
                            val pending = state.pendingSwitch
                            for (control in visible.sortedBy { if (it.index == pending) -1 else it.index }) {
                                if (!matches(current, control)) {
                                    if (++state.unstableReads >= 3) return stop(R.string.log_google_panel_changed)
                                    return continueAfter(530)
                                }
                                state.unstableReads = 0
                                when (GoogleSwitchImage.read(image, control.bounds)) {
                                    GoogleSwitchImage.State.OFF -> {
                                        state.unknownSwitch = null
                                        state.checked.add(control.index)
                                        if (pending == control.index) {
                                            ConsentActivityLog.record(
                                                packageName, R.string.log_google_switch_off,
                                                detail = control.label, subject = control.name
                                            )
                                            if (BuildConfig.DEBUG) Log.d(TAG, "Verified switch off: ${control.detail()}")
                                            state.pendingSwitch = null
                                        }
                                    }
                                    GoogleSwitchImage.State.ON -> {
                                        state.unknownSwitch = null
                                        if (pending != null && state.pendingSwitch != null) {
                                            if (state.pendingRechecks++ == 0) return continueAfter(410)
                                            return stop(R.string.log_google_change_failed, control.detail())
                                        }
                                        if (!gesture(control.bounds.centerX(), control.bounds.centerY())) {
                                            return stop(R.string.log_google_change_failed, control.detail())
                                        }
                                        state.pendingSwitch = control.index
                                        state.pendingRechecks = 0
                                        return continueAfter(410)
                                    }
                                    GoogleSwitchImage.State.UNKNOWN -> {
                                        state.unknownReads = if (state.unknownSwitch == control.index) {
                                            state.unknownReads + 1
                                        } else 1
                                        state.unknownSwitch = control.index
                                        if (BuildConfig.DEBUG) {
                                            Log.d(TAG, "Unclear switch: ${control.detail()} ${control.bounds}")
                                        }
                                        if (state.unknownReads >= 3) {
                                            return stop(R.string.log_google_state_unknown, control.detail())
                                        }
                                        return continueAfter(560)
                                    }
                                }
                            }
                            if (state.pendingSwitch != null) return stop(R.string.log_google_panel_changed)
                            advance(panel, state, displayed)
                        } finally {
                            image.recycle()
                        }
                    }
                }

                override fun onFailure(errorCode: Int) {
                    if (activeRoot() == null) return
                    if (errorCode == AccessibilityService.ERROR_TAKE_SCREENSHOT_INTERVAL_TIME_SHORT) {
                        continueAfter(460)
                    } else {
                        stop(R.string.log_google_capture_failed)
                    }
                }
            }
        )
    }

    private fun matches(panel: GoogleConsentPanel, control: GoogleConsentPanel.Control): Boolean {
        val row = panel.body.getChild(control.index) ?: return false
        val node = row.getChild(row.childCount - 1) ?: return false
        val bounds = Rect().also(node::getBoundsInScreen)
        val matches = node.className?.toString() == "android.widget.ToggleButton" &&
            node.isEnabled && bounds == control.bounds
        if (!matches && BuildConfig.DEBUG) {
            Log.d(TAG, "Switch moved: ${control.detail()} ${control.bounds} → $bounds, " +
                "enabled=${node.isEnabled}, visible=${node.isVisibleToUser}")
        }
        return matches
    }

    private fun advance(
        panel: GoogleConsentPanel,
        state: Progress,
        displayed: List<GoogleConsentPanel.Control>
    ) {
        if (state.checked.size != state.controls.size) return scroll(panel, state, displayed, forward = true)
        if (completed.add(panel.kind)) {
            val label = service.forAppLanguage().getString(
                if (panel.kind == GoogleConsentPanel.Kind.PURPOSES) R.string.google_purposes else R.string.google_vendors
            )
            ConsentActivityLog.record(packageName, R.string.log_google_panel_off, "$label · ${state.checked.size}")
            if (BuildConfig.DEBUG) Log.d(TAG, "Verified $label: ${state.checked.size} switches off")
        }
        if (completed.size == 2) {
            readyToConfirm {
                val root = activeRoot()
                val current = root?.let { GoogleConsentPanel.read(it, readControls = false) }
                if (current == null || !current.samePanel(panel) || !press(current.confirm)) {
                    ConsentActivityLog.record(packageName, R.string.log_google_panel_changed)
                    finish(R.string.result_incomplete)
                } else {
                    finish(R.string.result_google_verified)
                    if (BuildConfig.DEBUG) Log.d(TAG, "Confirmation sent")
                }
            }
            return
        }
        if (panel.kind == GoogleConsentPanel.Kind.VENDORS) {
            if (panel.back?.let(::press) != true) return stop(R.string.log_google_panel_changed)
            return continueAfter(650)
        }
        val link = panel.vendorLink ?: return scroll(panel, state, displayed, forward = true)
        if (!press(link)) return stop(R.string.log_google_panel_changed)
        continueAfter(650)
    }

    private fun scroll(
        panel: GoogleConsentPanel,
        state: Progress,
        displayed: List<GoogleConsentPanel.Control>,
        forward: Boolean
    ) {
        if (activeRoot() == null) return
        val signature = displayed.map { it.index to it.bounds }
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "${panel.kind} scroll forward=$forward top=${panel.top} " +
                "verified=${state.checked.size}/${state.controls.size} visible=$signature")
        }
        state.repeatedViewport = if (state.lastViewport == signature) state.repeatedViewport + 1 else 0
        state.lastViewport = signature
        if (state.repeatedViewport >= 3) return stop(R.string.log_google_inventory_unknown)
        val bounds = panel.viewport
        val upper = bounds.top + bounds.height() * 22 / 100
        val lower = bounds.bottom - bounds.height() * 22 / 100
        val x = bounds.left + bounds.width() / 12
        val action = if (forward) AccessibilityNodeInfo.ACTION_SCROLL_FORWARD else
            AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD
        val moved = if (state.repeatedViewport == 0) {
            panel.body.performAction(action)
        } else {
            gesture(x, if (forward) lower else upper, if (forward) upper else lower)
        }
        if (!moved && !gesture(x, if (forward) lower else upper, if (forward) upper else lower)) {
            return stop(R.string.log_google_panel_changed)
        }
        continueAfter(750)
    }

    private fun press(action: GoogleConsentPanel.Action): Boolean {
        val root = activeRoot() ?: return false
        val node = action.node
        if (node.windowId != root.windowId || !node.refresh() || !node.isVisibleToUser ||
            !node.isEnabled || !node.isClickable ||
            node.text?.toString()?.trim().orEmpty().ifEmpty {
                node.contentDescription?.toString()?.trim().orEmpty()
            } != action.label
        ) return false
        val bounds = Rect().also(node::getBoundsInScreen)
        return !bounds.isEmpty && gesture(bounds.centerX(), bounds.centerY())
    }

    private fun gesture(x: Int, startY: Int, endY: Int = startY): Boolean {
        if (activeRoot() == null || !canTouch(x, startY) || !canTouch(x, endY)) return false
        val path = Path().apply {
            moveTo(x.toFloat(), startY.toFloat())
            if (endY != startY) lineTo(x.toFloat(), endY.toFloat())
        }
        val stroke = GestureDescription.StrokeDescription(path, 0, if (endY == startY) 55 else 470)
        return service.dispatchGesture(GestureDescription.Builder().addStroke(stroke).build(), null, null)
    }

    private fun continueAfter(delay: Long) {
        waiting = true
        handler.postDelayed({
            waiting = false
            if (activeRoot() != null) {
                if (Build.VERSION.SDK_INT >= 33) service.clearCache()
                nextScan(0)
            }
        }, delay)
    }

    private fun stop(message: Int, detail: String? = null) {
        if (activeRoot() == null) return
        ConsentActivityLog.record(packageName, message, detail)
        if (BuildConfig.DEBUG) {
            val text = if (detail == null) service.getString(message) else service.getString(message, detail)
            Log.d(TAG, text)
        }
        finish(R.string.result_vendor_review)
    }

    private fun GoogleConsentPanel.Control.detail() = "$name · $label"

    private companion object {
        const val TAG = "CookieRejecterReview"
    }
}
