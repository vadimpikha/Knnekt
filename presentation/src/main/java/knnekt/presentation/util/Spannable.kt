package knnekt.presentation.util

import android.text.SpannableString
import android.widget.TextView

fun TextView.applySpan(span: Any) {
    require(!text.isNullOrEmpty())
    val spannable = SpannableString(text)
    spannable.setSpan(span, 0, text.length, 0)
    text = spannable
}