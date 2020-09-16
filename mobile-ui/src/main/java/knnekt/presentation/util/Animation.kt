package knnekt.presentation.util

import android.animation.Animator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Animation

private open class AnimationListenerImpl : Animation.AnimationListener {
    override fun onAnimationStart(p0: Animation?) {}
    override fun onAnimationEnd(p0: Animation?) {}
    override fun onAnimationRepeat(p0: Animation?) { }
}

fun ViewPropertyAnimator.disableWhileAnimation(view: View) {
    setListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator?) {
            view.isClickable = false
        }
        override fun onAnimationEnd(animation: Animator?) {
            view.isClickable = true
        }
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationRepeat(animation: Animator?) {}
    })
}


fun Animation.onStart(block: () -> Unit) {
    setAnimationListener(object : AnimationListenerImpl() {
        override fun onAnimationStart(p0: Animation?) {
            block()
        }
    })
}

fun Animation.onEnd(block: () -> Unit) {
    setAnimationListener(object : AnimationListenerImpl() {
        override fun onAnimationEnd(p0: Animation?) {
            block()
        }
    })
}