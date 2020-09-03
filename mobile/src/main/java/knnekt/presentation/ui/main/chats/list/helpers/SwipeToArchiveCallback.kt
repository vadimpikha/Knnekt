package knnekt.presentation.ui.main.chats.list.helpers

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import knnekt.R
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter
import knnekt.presentation.util.themeColor

open class SwipeToArchiveCallback(
    context: Context,
    private val adapter: ChatsListAdapter
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    var onSwipe: Boolean = false
        private set


    private val iconColor = context.themeColor(android.R.attr.textColorPrimaryInverse)
    private val icon =
        ContextCompat.getDrawable(context, R.drawable.ic_outline_archive_24)!!.apply {
            setTint(iconColor)
        }
    private val background = ColorDrawable(context.themeColor(R.attr.colorSecondary))

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwipe = false
        adapter.deleteItem(viewHolder.bindingAdapterPosition)
    }

    override fun isLongPressDragEnabled() = false

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20

        val iconMargin: Int = (itemView.height - icon.intrinsicHeight) / 2
        val iconTop: Int = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom: Int = iconTop + icon.intrinsicHeight

        if (dX < 0) { // Swiping to the left
            onSwipe = true
            val iconLeft: Int = itemView.right - iconMargin - icon.intrinsicWidth
            val iconRight = itemView.right - iconMargin
            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)
            background.setBounds(
                itemView.right + dX.toInt() - backgroundCornerOffset,
                itemView.top, itemView.right, itemView.bottom
            )
        } else { // view is unSwiped
            onSwipe = false
            icon.setBounds(0, 0, 0, 0);
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }

}