package knnekt.presentation.ui

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.annotation.Dimension
import androidx.recyclerview.widget.RecyclerView
import knnekt.presentation.util.dp

class MarginItemDecorator(
    context: Context,
    @Dimension(unit = Dimension.DP)
    margin: Int,
    private val reversed: Boolean = false
) : RecyclerView.ItemDecoration() {

    private val marginPx = context.dp(margin)

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }

        val first = itemPosition == 0
        val last = itemPosition == (parent.adapter?.itemCount ?: 0) - 1

        with(outRect) {
            if (first) {
                if (reversed) {
                    bottom = marginPx
                } else {
                    top = marginPx
                }
            }

            if(last) {
                if (reversed) {
                    top = marginPx
                } else {
                    bottom = marginPx
                }
            }

            left = marginPx
            right = marginPx
            bottom = marginPx
        }
    }

}