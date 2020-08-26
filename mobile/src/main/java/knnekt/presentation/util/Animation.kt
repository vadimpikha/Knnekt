package knnekt.presentation.util

import android.animation.Animator
import android.view.View
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.disableWhileAnimation(view: View) {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            view.isEnabled = false
        }
        override fun onAnimationEnd(animation: Animator?) {
            view.isEnabled = true
        }
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    })
}