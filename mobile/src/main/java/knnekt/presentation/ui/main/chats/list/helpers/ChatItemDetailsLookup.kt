package knnekt.presentation.ui.main.chats.list.helpers

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter
import knnekt.shared.data.entity.Chat

class ChatItemDetailsLookup(
    private val recyclerView: RecyclerView
): ItemDetailsLookup<Chat>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Chat>? {
        val view = recyclerView.findChildViewUnder(e.x, e.y)

        if (view != null) {
            val holder = recyclerView.getChildViewHolder(view)
            if (holder is ChatsListAdapter.ChatViewHolder) {
                return holder.getItemDetails()
            }
        }

        return null
    }

}