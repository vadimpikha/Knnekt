package knnekt.presentation.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import knnekt.presentation.util.dp

class MarginItemDecoration(
    context: Context,
    @Dimension(unit = Dimension.DP)
    margin: Int,
    private val reversed: Boolean = false
) : RecyclerView.ItemDecoration() {

    private var allSides = false

    companion object {
        @Suppress("FunctionName")
        fun AllSides(
            context: Context,
            @Dimension(unit = Dimension.DP)
            margin: Int
        ) = MarginItemDecoration(context, margin).apply {
            allSides = true
        }
    }


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
            if (first && !allSides) {
                if (reversed) {
                    bottom = marginPx
                } else {
                    top = marginPx
                }
            }

            if (last && !allSides) {
                if (reversed) {
                    top = marginPx
                } else {
                    bottom = marginPx
                }
            }

            if (allSides) {
                top = marginPx
            }
            left = marginPx
            right = marginPx
            bottom = marginPx
        }
    }

}

class SenderAvatarDecoration() : RecyclerView.ItemDecoration() {

}

class MassageDateDecoration(
    val listener: StickyListener
) : RecyclerView.ItemDecoration() {

    var headerHeight: Int = 0

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val topChild = parent.getChildAt(0) ?: return

        val topChildPosition = parent.getChildAdapterPosition(topChild)
        if (topChildPosition == RecyclerView.NO_POSITION) return

        val headerPos = listener.getHeaderPositionForItem(topChildPosition)
        val currentHeader = getHeaderViewForItem(headerPos, parent)
        fixLayoutSize(parent, currentHeader)
        val contactPoint = currentHeader.bottom
        val childInContact = getChildInContact(parent, contactPoint, headerPos)

        if (childInContact != null && listener.isHeader(
                parent.getChildAdapterPosition(
                    childInContact
                )
            )
        ) {
            moveHeader(c, currentHeader, childInContact)
            return
        }

        drawHeader(c, currentHeader)
    }

    private fun getHeaderViewForItem(headerPosition: Int, parent: RecyclerView): View {
        val layoutResId = listener.getHeaderLayout(headerPosition)
        val header = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        listener.bindHeaderData(header, headerPosition)
        return header
    }

    private fun drawHeader(canvas: Canvas, header: View) {
        canvas.save()
        canvas.translate(0f, 0f)
        header.draw(canvas)
        canvas.restore()
    }

    private fun moveHeader(canvas: Canvas, currentHeader: View, nextHeader: View) {
        canvas.save()
        canvas.translate(0f, (nextHeader.top - currentHeader.height).toFloat())
        currentHeader.draw(canvas)
        canvas.restore()
    }

    private fun getChildInContact(
        parent: RecyclerView,
        contactPoint: Int,
        currentHeaderPos: Int
    ): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            var heightTolerance = 0
            val child = parent.getChildAt(i)

            if (currentHeaderPos != i) {
                val isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child))
                if (isChildHeader) {
                    heightTolerance = headerHeight - child.height
                }
            }

            val childBottomPosition = if (child.top > 0) {
                child.bottom + heightTolerance
            } else {
                child.bottom
            }

            if (childBottomPosition > contactPoint) {
                if (child.top <= contactPoint) {
                    childInContact = child
                    break
                }
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {

        // Specs for parent (RecyclerView)
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec =
            View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        // Specs for children (headers)
        val childWidthSpec =
            ViewGroup.getChildMeasureSpec(
                widthSpec,
                parent.paddingLeft + parent.paddingRight,
                view.layoutParams.width
            )
        val childHeightSpec = ViewGroup.getChildMeasureSpec(
            heightSpec,
            parent.paddingTop + parent.paddingBottom,
            view.layoutParams.height
        )

        view.measure(childWidthSpec, childHeightSpec)
        headerHeight = view.measuredHeight
        view.layout(0, 0, view.measuredWidth, headerHeight)
    }

}