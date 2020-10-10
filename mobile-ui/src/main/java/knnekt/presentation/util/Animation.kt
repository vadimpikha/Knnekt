package knnekt.presentation.util

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import knnekt.R

private open class AnimationListenerImpl : Animation.AnimationListener {
    override fun onAnimationStart(p0: Animation?) {}
    override fun onAnimationEnd(p0: Animation?) {}
    override fun onAnimationRepeat(p0: Animation?) {}
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


object FlipAnimator {
    /**
     * Performs flip animation on two views
     */
    fun flipView(context: Context, back: View, front: View, showFront: Boolean) {
        val leftIn =
            AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_in) as AnimatorSet
        val rightOut =
            AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_out) as AnimatorSet
        val leftOut =
            AnimatorInflater.loadAnimator(context, R.animator.card_flip_left_out) as AnimatorSet
        val rightIn =
            AnimatorInflater.loadAnimator(context, R.animator.card_flip_right_in) as AnimatorSet

        val showFrontAnim = AnimatorSet()
        val showBackAnim = AnimatorSet()

        leftIn.setTarget(back)
        rightOut.setTarget(front)
        showFrontAnim.playTogether(leftIn, rightOut)
        leftOut.setTarget(back)
        rightIn.setTarget(front)
        showBackAnim.playTogether(rightIn, leftOut)

        if (showFront) {
            showFrontAnim.start()
        } else {
            showBackAnim.start()
        }
    }
}