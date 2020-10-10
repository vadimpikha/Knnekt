package knnekt.presentation.ui.main.chats.list.helpers

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter

class ChatItemDetailsLookup(
    private val recycler: RecyclerView
) : ItemDetailsLookup<String>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<String>? {
        val view = recycler.findChildViewUnder(e.x, e.y)
        if(view != null) {
           return (recycler.getChildViewHolder(view) as ChatsListAdapter.ChatViewHolder)
                .getItemDetails()
        }
        return null
    }
}