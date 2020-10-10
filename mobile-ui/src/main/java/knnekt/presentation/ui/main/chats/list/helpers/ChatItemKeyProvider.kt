package knnekt.presentation.ui.main.chats.list.helpers

import androidx.recyclerview.selection.ItemKeyProvider
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter

class ChatItemKeyProvider(private val adapter: ChatsListAdapter) : ItemKeyProvider<String>(SCOPE_MAPPED) {

    override fun getKey(position: Int): String? {
        return adapter.getItemAtPosition(position)?.id
    }

    override fun getPosition(key: String): Int {
        return adapter.getPositionOf(key)
    }


}