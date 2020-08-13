package knnekt.presentation.util

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.*
import androidx.fragment.app.Fragment

fun EditText.onActionDone(block: EditText.() -> Unit) =
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            block()
            return@setOnEditorActionListener true
        }
        false
    }

fun Context.toast(@StringRes resId: Int, long: Boolean = false) {
    val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, resId, duration).show()
}

fun Fragment.toast(@StringRes resId: Int, long: Boolean = false) {
    requireContext().toast(resId, long)
}

fun Context.toast(text: String, long: Boolean = false) {
    val duration = if (long) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    Toast.makeText(this, text, duration).show()
}

fun Fragment.toast(text: String, long: Boolean = false) {
    requireContext().toast(text, long)
}

fun Activity.configureDecorView() {
    val decorView = window?.decorView
    decorView?.systemUiVisibility =
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    val nightMode = isNightMode

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        decorView?.apply {
            systemUiVisibility = if (nightMode)
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            else
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        decorView?.apply {
            systemUiVisibility = if (nightMode)
                systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
            else
                systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }
}

val Context.isNightMode: Boolean
    get() {
        val defaultNightMode = AppCompatDelegate.getDefaultNightMode()
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            return true
        }
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            return false
        }

        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> return false
            Configuration.UI_MODE_NIGHT_YES -> return true
            Configuration.UI_MODE_NIGHT_UNDEFINED -> return false
        }
        return false
    }

fun View.updateMargin(
    left: Int? = null,
    top: Int? = null,
    right: Int? = null,
    bottom: Int? = null
) {
    val params = layoutParams
    require(params is ViewGroup.MarginLayoutParams) { "You can use updateMargins method only for views with MarginLayoutParams" }

    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        left?.let { leftMargin = it }
        top?.let { topMargin = it }
        right?.let { rightMargin = it }
        bottom?.let { bottomMargin = it }
    }
}

fun Context.dp(value: Number) = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,
    value.toFloat(),
    resources.displayMetrics
).toInt()

fun Fragment.dp(value: Number) = requireContext().dp(value)

fun View.onClick(disableWhileOp: Boolean = false, op: View.() -> Unit) {
    setOnClickListener {
        if (disableWhileOp) {
            isEnabled = false
            op()
            isEnabled = true
        } else {
            op()
        }
    }
}

fun View.requestApplyInsetsWhenAttached() {
    doOnAttach {
        it.requestApplyInsets()
    }
}

fun View.doOnApplyWindowInsets(f: (View, WindowInsets, InitialPadding) -> Unit) {
    // Create a snapshot of the view's padding state
    val initialPadding = recordInitialPaddingForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialPadding)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

fun View.doOnApplyWindowInsetsMargins(f: (View, WindowInsets, InitialMargin) -> Unit) {
    // Create a snapshot of the view's margin state
    val initialMargin = recordInitialMarginForView(this)
    // Set an actual OnApplyWindowInsetsListener which proxies to the given
    // lambda, also passing in the original padding state
    setOnApplyWindowInsetsListener { v, insets ->
        f(v, insets, initialMargin)
        // Always return the insets, so that children can also use them
        insets
    }
    // request some insets
    requestApplyInsetsWhenAttached()
}

data class InitialPadding(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

data class InitialMargin(
    val left: Int, val top: Int,
    val right: Int, val bottom: Int
)

private fun recordInitialPaddingForView(view: View) = InitialPadding(
    view.paddingLeft, view.paddingTop, view.paddingRight, view.paddingBottom
)

private fun recordInitialMarginForView(view: View) = InitialMargin(
    view.marginLeft, view.marginTop, view.marginRight, view.marginBottom
)

fun Fragment.setActionBar(toolbar: Toolbar) {
    (activity as? AppCompatActivity)?.setSupportActionBar(toolbar)
}
