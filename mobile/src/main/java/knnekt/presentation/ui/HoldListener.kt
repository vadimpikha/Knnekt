package knnekt.presentation.ui

import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.os.postDelayed


interface HoldListener {
    fun onHold(view: View)
    fun onReleased(view: View)
}

fun View.setOnHoldListener(listener: HoldListener) {
    var lastDownTimeMillis = 0L

    val handler = Handler()

    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.isPressed = true
                lastDownTimeMillis = System.currentTimeMillis()
                handler.postDelayed(ViewConfiguration.getLongPressTimeout().toLong()) {
                    listener.onHold(v)
                }
                return@setOnTouchListener true
            }

            MotionEvent.ACTION_UP -> {
                v.isPressed = false
                handler.removeCallbacksAndMessages(null)
                if (System.currentTimeMillis() - lastDownTimeMillis >= ViewConfiguration.getLongPressTimeout()) {
                    listener.onReleased(v)
                } else {
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