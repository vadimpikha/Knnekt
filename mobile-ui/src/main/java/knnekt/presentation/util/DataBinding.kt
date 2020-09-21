package knnekt.presentation.util

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.view.*
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.badge.BadgeDrawable
import knnekt.R
import knnekt.domain.entity.Chat
import knnekt.domain.entity.ChatType
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@BindingAdapter(
    "app:asyncText",
    "android:textSize",
    requireAll = false
)
fun asyncText(view: TextView, text: CharSequence, textSize: Int?) {
    // first, set all measurement affecting properties of the text
    // (size, locale, typeface, direction, etc)
    if (textSize != null) {
        // interpret the text size as SP
        view.textSize = textSize.toFloat()
    }
    val params = TextViewCompat.getTextMetricsParams(view)
    (view as AppCompatTextView).setTextFuture(
        PrecomputedTextCompat.getTextFuture(text, params, null)
    )
}

@BindingAdapter("textInt")
fun TextView.setTextInt(value: Int) {
    text = value.toString()
}

@BindingAdapter("activated")
fun activated(view: View, state: Boolean) {
    view.isActivated = state
}

@BindingAdapter("imageUri", "placeholder", requireAll = false)
fun ImageView.setImage(uri: String?, placeholder: Drawable?) {
    Glide.with(this)
        .load(uri)
        .placeholder(placeholder)
        .error(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@BindingAdapter("bindChatPhoto")
fun ImageView.setChatPhoto(chat: Chat?) {

    chat ?: return

    val uri = chat.photo
    val placeholder = if (chat.type == ChatType.PRIVATE) R.drawable.ic_avatar_placeholder
    else R.drawable.ic_avatar_placeholder_group

    Glide.with(this)
        .load(uri)
        .placeholder(placeholder)
        .error(placeholder)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}


@BindingAdapter("attachmentImage", "attachmentPlaceholder", requireAll = false)
fun ImageView.setPicture(uri: String?, placeholder: Drawable?) {

    val width = context.dp(200)
    val height = context.dp(300)

    Glide.with(this)
        .load(uri)
        .placeholder(placeholder)
        .override(width, height)
        .dontTransform()
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(this)
}

@BindingAdapter(
    "paddingLeftSystemWindowInsets",
    "paddingTopSystemWindowInsets",
    "paddingRightSystemWindowInsets",
    "paddingBottomSystemWindowInsets",
    requireAll = false
)
fun applySystemWindows(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    view.doOnApplyWindowInsets { _, insets, padding ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.setPadding(
            padding.left + left,
            padding.top + top,
            padding.right + right,
            padding.bottom + bottom
        )
    }
}


@BindingAdapter(
    "marginLeftSystemWindowInsets",
    "marginTopSystemWindowInsets",
    "marginRightSystemWindowInsets",
    "marginBottomSystemWindowInsets",
    requireAll = false
)
fun applySystemWindowsMargins(
    view: View,
    applyLeft: Boolean,
    applyTop: Boolean,
    applyRight: Boolean,
    applyBottom: Boolean
) {
    view.doOnApplyWindowInsetsMargins { _, insets, margin ->
        val left = if (applyLeft) insets.systemWindowInsetLeft else 0
        val top = if (applyTop) insets.systemWindowInsetTop else 0
        val right = if (applyRight) insets.systemWindowInsetRight else 0
        val bottom = if (applyBottom) insets.systemWindowInsetBottom else 0

        view.updateMargin(
            margin.left + left,
            margin.top + top,
            margin.right + right,
            margin.bottom + bottom
        )
    }
}

@BindingAdapter("visibleWhen")
fun View.setVisible(value: Boolean) {
    if (isVisible == value) return
    isVisible = value
}

@BindingAdapter("changeSizeWhen", "otherSize", "initialSize")
fun View.setExpanded(value: Boolean, otherSize: Float, initialSize: Float) {
    val measuredWidth = this.measuredWidth.toFloat()
    if (value) {
        if (otherSize == measuredWidth) return
    } else {
        if (initialSize == measuredWidth) return
    }

    val animator = ObjectAnimator.ofFloat(measuredWidth, if (value) otherSize else initialSize)

    animator.addUpdateListener {
        val size = it.animatedValue as Float
        updateLayoutParams<ViewGroup.LayoutParams> {
            width = size.toInt()
        }
    }

    animator.duration = 200
    animator.start()
}

@BindingAdapter("invisibleWhen")
fun View.setInvisible(value: Boolean) {
    if (isInvisible == value) return
    isInvisible = value
}

@BindingAdapter("showWhen")
fun showWhen(view: View, value: Boolean) {
    if (view.isVisible == value) return

    if (value) {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.scale_appear).apply {
            interpolator = LinearOutSlowInInterpolator()
            onStart { view.isVisible = true }
        }

        view.startAnimation(animation)
    } else {
        val animation = AnimationUtils.loadAnimation(view.context, R.anim.scale_disappear).apply {
            interpolator = LinearOutSlowInInterpolator()
            onEnd { view.isGone = true }
        }

        view.startAnimation(animation)
    }
}

class FragmentViewBindingDelegate<T : ViewBinding>(
    private val fragment: Fragment,
    val viewBindingFactory: (View) -> T
) : ReadOnlyProperty<Fragment, T> {
    private var binding: T? = null

    init {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
                    viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
                        override fun onDestroy(owner: LifecycleOwner) {
                            binding = null
                        }
                    })
                }
            }
        })
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val binding = binding
        if (binding != null) {
            return binding
        }

        val lifecycle = fragment.viewLifecycleOwner.lifecycle
        if (lifecycle.currentState < Lifecycle.State.INITIALIZED) {
            throw IllegalStateException("Should not attempt to get bindings when Fragment views are destroyed.")
        }

        return viewBindingFactory(thisRef.requireView()).also {
            this@FragmentViewBindingDelegate.binding = it
        }
    }
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)


inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }


