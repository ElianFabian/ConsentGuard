package dev.macbuildssys.consentguard

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.GradientDrawable
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import com.google.android.material.R as MaterialR
import com.google.android.material.button.MaterialButton
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.MaterialColors
import com.google.android.material.textview.MaterialTextView
import kotlin.math.roundToInt

internal class ConsentPrompt(private val service: AccessibilityService) {
    private enum class ActionColors(val background: Int, val foreground: Int) {
        TONAL(MaterialR.attr.colorSecondaryContainer, MaterialR.attr.colorOnSecondaryContainer),
        ERROR(MaterialR.attr.colorError, MaterialR.attr.colorOnError)
    }

    private val windows = service.getSystemService(WindowManager::class.java)
    private var overlay: View? = null
    @Volatile private var bounds: Rect? = null

    fun show(onApprove: () -> Unit) {
        attach(action(themedContext(), R.string.reject_optional, onClick = onApprove))
    }

    fun showControls(paused: Boolean, onPauseOrResume: () -> Unit, onStop: () -> Unit) {
        val context = themedContext()
        val pauseOrResume = action(
            context,
            if (paused) R.string.resume_run else R.string.pause_run,
            if (paused) R.drawable.ic_run_resume else R.drawable.ic_run_pause,
            ActionColors.TONAL,
            onPauseOrResume
        )
        attach(buttonRow(context, pauseOrResume, stopButton(context, onStop)))
    }

    fun showConfirmation(onConfirm: () -> Unit, onStop: () -> Unit) {
        val context = themedContext()
        val panel = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            val padding = (8 * resources.displayMetrics.density).roundToInt()
            setPadding(padding, padding, padding, 0)
            background = GradientDrawable().apply {
                setColor(MaterialColors.getColor(context, MaterialR.attr.colorSurface, Color.WHITE))
                cornerRadius = 20 * resources.displayMetrics.density
            }
            addView(MaterialTextView(context).apply {
                setText(R.string.confirm_run_ready)
                setTextAppearance(MaterialR.style.TextAppearance_Material3_LabelLarge)
                gravity = Gravity.CENTER
            })
            addView(buttonRow(
                context, action(context, R.string.confirm_run, onClick = onConfirm), stopButton(context, onStop)
            ))
        }
        attach(panel)
    }

    fun covers(x: Int, y: Int): Boolean = bounds?.contains(x, y) == true

    private fun themedContext(): Context =
        DynamicColors.wrapContextIfAvailable(
            ContextThemeWrapper(service.forAppLanguage(), R.style.CookieRejecterTheme)
        )

    private fun buttonRow(context: Context, primary: MaterialButton, stop: MaterialButton): LinearLayout =
        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            addView(primary)
            addView(stop, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { marginStart = (8 * resources.displayMetrics.density).roundToInt() })
        }

    private fun stopButton(context: Context, onStop: () -> Unit): MaterialButton =
        action(context, R.string.stop_run, R.drawable.ic_run_stop, ActionColors.ERROR, onStop)

    private fun action(
        context: Context,
        label: Int,
        symbol: Int = 0,
        colors: ActionColors? = null,
        onClick: () -> Unit
    ): MaterialButton =
        MaterialButton(context).apply {
            setText(label)
            isAllCaps = false
            if (symbol != 0) setIconResource(symbol)
            colors?.let {
                val foreground = MaterialColors.getColor(this, it.foreground)
                backgroundTintList = ColorStateList.valueOf(MaterialColors.getColor(this, it.background))
                setTextColor(foreground)
                iconTint = ColorStateList.valueOf(foreground)
            }
            setOnClickListener {
                isEnabled = false
                onClick()
            }
        }

    private fun attach(view: View) {
        hide()
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.BOTTOM or Gravity.END
            val density = service.resources.displayMetrics.density
            x = (16 * density).roundToInt()
            y = (32 * density).roundToInt()
            title = service.getString(R.string.app_title)
        }
        try {
            windows.addView(view, params)
            overlay = view
            view.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
                if (overlay === view) {
                    val location = IntArray(2)
                    view.getLocationOnScreen(location)
                    bounds = Rect(location[0], location[1], location[0] + view.width, location[1] + view.height)
                }
            }
        } catch (_: WindowManager.BadTokenException) {
            // The service can lose its overlay token while being disabled.
        } catch (_: WindowManager.InvalidDisplayException) {
            // Android can remove the active display during a transition.
        }
    }

    fun hide() {
        val view = overlay ?: return
        overlay = null
        bounds = null
        if (view.isAttachedToWindow) windows.removeViewImmediate(view)
    }
}
