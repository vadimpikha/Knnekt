package knnekt.presentation.ui.main.chats.messages

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import knnekt.presentation.entity.MessageItem
import kotlinx.android.extensions.LayoutContainer

class ChatAdapter : ListAdapter<MessageItem, ChatAdapter.ChatMessageViewHolder>(MessageDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    class  ChatMessageViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(message: MessageItem) {

        }

    }

    object MessageDiff: DiffUtil.ItemCallback<MessageItem>() {
        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            TODO("Not yet implemented")
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            TODO("Not yet implemented")
        }

    }
}