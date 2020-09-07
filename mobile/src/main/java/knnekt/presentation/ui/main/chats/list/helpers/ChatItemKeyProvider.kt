package knnekt.presentation.ui.main.chats.list.helpers

import androidx.recyclerview.selection.ItemKeyProvider
import knnekt.presentation.ui.main.chats.list.ChatsListAdapter
import knnekt.shared.data.entity.Chat

class ChatItemKeyProvider(
    private val adapter: ChatsListAdapter
) : ItemKeyProvider<Chat>(SCOPE_MAPPED) {

    override fun getKey(position: Int): Chat? {
        return adapter.getItemAtPosition(position)
    }

    override fun getPosition(key: Chat): Int {
        return adapter.getPosition(key)
    }

}