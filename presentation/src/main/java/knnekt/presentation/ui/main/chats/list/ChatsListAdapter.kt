package knnekt.presentation.ui.main.chats.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import knnekt.R
import knnekt.presentation.entity.ChatItem
import knnekt.presentation.util.onClick
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_chat_list.*
import kotlin.properties.Delegates

class ChatsListAdapter(
    private val onClick: (ChatItem) -> Unit = {}
) : ListAdapter<ChatItem, ChatsListAdapter.ChatViewHolder>(ChatDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_list, parent, false)
        return ChatViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.onClick(true){
            onClick.invoke(item)
        }
    }

    class ChatViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView),
        LayoutContainer {


        fun bind(chat: ChatItem) {

            val placeholder = if (chat.isPrivate)
                    R.drawable.ic_avatar_placeholder
                else
                    R.drawable.ic_avatar_placeholder_group

            Glide.with(containerView)
                .load(chat.photo)
                .placeholder(placeholder)
                .error(placeholder)
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(chat_photo)

            chat_name.text = chat.name
            chat_last_message.text = chat.lastMessage
            unread_messages_badge.text = chat.unreadMessageCount
            unread_messages_badge.isInvisible = chat.unreadMessageCount.isEmpty()
            last_message_time.text = chat.updatedAt
        }
    }

    object ChatDiff : DiffUtil.ItemCallback<ChatItem>() {

        override fun areItemsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ChatItem, newItem: ChatItem): Boolean {
            return oldItem.lastMessage == newItem.lastMessage
                    && oldItem.name == newItem.name
                    && oldItem.photo == newItem.photo
                    && oldItem.updatedAt == newItem.updatedAt
                    && oldItem.unreadMessageCount == newItem.unreadMessageCount
        }

    }
}