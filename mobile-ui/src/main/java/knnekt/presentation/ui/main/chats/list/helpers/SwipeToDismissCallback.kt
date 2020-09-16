package knnekt.presentation.ui.main.chats.list.helpers

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView


abstract class SwipeToDismissCallback(
    private val icon: Int,
    private val backgroundColor: Int,
    private val text: String,
    private val elementsColor: Int
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    var whileSwipe: Boolean = false
        private set

    private var decorator: RecyclerViewSwipeDecorator? = null

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    final override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onSwiped(viewHolder.bindingAdapterPosition)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)
        whileSwipe = actionState == ItemTouchHelper.ACTION_STATE_SWIPE
    }

    abstract fun onSwiped(position: Int)

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

        decorator = RecyclerViewSwipeDecorator.Builder(
            c,
            recyclerView,
            viewHolder,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
            .addSwipeLeftActionIcon(icon)
            .addSwipeLeftLabel(text)
            .setSwipeLeftActionIconTint(elementsColor)
            .addSwipeLeftBackgroundColor(backgroundColor)
            .setSwipeLeftLabelColor(elementsColor)
            .create().also {
                it.decorate()
            }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        decorator?.clear()
    }

}