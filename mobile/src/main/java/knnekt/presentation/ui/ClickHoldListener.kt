package knnekt.presentation.ui

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.os.postDelayed
import knnekt.R


fun interface ShortClickListener {
    fun onShortClick(view: View)
}

interface HoldListener {
    fun onHold(view: View)
    fun onReleased(view: View)
}

private class ClickHoldListener(
    var shortClickListener: ShortClickListener? = null,
    var holdListener: HoldListener? = null
) : ShortClickListener, HoldListener {

    override fun onShortClick(view: View) {
        shortClickListener?.onShortClick(view)
    }

    override fun onHold(view: View) {
        holdListener?.onHold(view)
    }

    override fun onReleased(view: View) {
        holdListener?.onReleased(view)
    }

}

fun View.setOnShortClickListener(listener: ShortClickListener) {
    var clickHoldListener = getTag(R.id.TAG_CLICK_HOLD_LISTENER) as? ClickHoldListener

    if (clickHoldListener != null) {
        clickHoldListener.shortClickListener = listener
    } else {
        clickHoldListener = ClickHoldListener(shortClickListener = listener)
        setTag(R.id.TAG_CLICK_HOLD_LISTENER, clickHoldListener)
    }

    setClickHoldListener(clickHoldListener)
}

fun View.setOnHoldListener(listener: HoldListener) {
    var clickHoldListener = getTag(R.id.TAG_CLICK_HOLD_LISTENER) as? ClickHoldListener

    if (clickHoldListener != null) {
        clickHoldListener.holdListener = listener
    } else {
        clickHoldListener = ClickHoldListener(holdListener = listener)
        setTag(R.id.TAG_CLICK_HOLD_LISTENER, clickHoldListener)
    }
    setClickHoldListener(clickHoldListener)
}

private fun View.setClickHoldListener(listener: ClickHoldListener) {

    var lastDownTimeMillis = 0L

    val handler = Handler()

    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastDownTimeMillis = System.currentTimeMillis()
                handler.postDelayed(ViewConfiguration.getLongPressTimeout().toLong()) {
                    listener.onHold(v)
                }
                return@setOnTouchListener true
            }

            MotionEvent.ACTION_UP -> {
                handler.removeCallbacksAndMessages(null)
                if (System.currentTimeMillis() - lastDownTimeMillis >= ViewConfiguration.getLongPressTimeout()) {
                    listener.onReleased(v)
                } else {
                    listener.onShortClick(v)
                    v.performClick()
                }
                return@setOnTouchListener true
            }
            MotionEvent.ACTION_CANCEL -> {
                handler.removeCallbacksAndMessages(null)
                if (System.currentTimeMillis() - lastDownTimeMillis > ViewConfiguration.getLongPressTimeout()) {
                    listener.onReleased(v)
                }
                return@setOnTouchListener true
            }
        }

        false
    }
}