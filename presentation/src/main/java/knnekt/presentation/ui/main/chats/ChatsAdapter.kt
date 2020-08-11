package knnekt.presentation.ui.main.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import knnekt.R
import knnekt.domain.entity.Chat
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_list.*
import kotlin.properties.Delegates

class ChatsAdapter : ListAdapter<Chat, ChatsAdapter.ChatViewHolder>(ChatDiff) {

    var chats: List<Chat> by Delegates.observable(emptyList()) { _, _, new ->
        submitList(new)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chats[position])
    }

    class ChatViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bind(chat: Chat) {
            Glide.with(containerView)
                .load(chat.photo)
                .into(chat_photo)

            chat_name.text = chat.name
        }
    }

    object ChatDiff : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.lastMessage == newItem.lastMessage
                    && oldItem.lastMessageDateSent == newItem.lastMessageDateSent
                    && oldItem.name == newItem.name
                    && oldItem.occupants == newItem.occupants
                    && oldItem.photo == newItem.photo
                    && oldItem.createdAt == newItem.createdAt
                    && oldItem.updatedAt == newItem.updatedAt
                    && oldItem.unreadMessageCount == newItem.unreadMessageCount
        }

    }
}