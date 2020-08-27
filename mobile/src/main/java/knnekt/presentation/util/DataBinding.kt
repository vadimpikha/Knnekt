package knnekt.presentation.util

import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
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
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import knnekt.R
import knnekt.shared.data.entity.Chat
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty


@BindingAdapter("bindChat")
fun Toolbar.bindChat(chat: Chat?) {
    if (chat == null) return

    val image = findViewById<ImageView>(R.id.chat_photo)
    val status = findViewById<TextView>(R.id.status)
    val chatName = findViewById<TextView>(R.id.chat_name)

    val placeholder = if (chat.isPrivate)
        R.drawable.ic_avatar_placeholder
    else
        R.drawable.ic_avatar_placeholder_group

    Glide.with(this)
        .load(chat.photo)
        .placeholder(placeholder)
        .error(placeholder)
        .apply(RequestOptions.circleCropTransform())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(image)

    chatName.text = chat.name
//    onlineStatus.text = chat.name
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

@BindingAdapter("visible")
fun setVisibility(view: View, value: Boolean) {
    val newVisibility = if (value) View.VISIBLE else View.GONE
    if (view.visibility != newVisibility)
        view.visibility = newVisibility
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


