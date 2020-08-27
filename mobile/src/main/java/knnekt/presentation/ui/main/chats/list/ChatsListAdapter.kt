package knnekt.presentation.ui.main.chats.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import knnekt.BR
import knnekt.R
import knnekt.presentation.util.onClick
import knnekt.shared.data.entity.Chat

class ChatsListAdapter(
    private val onClick: (Chat) -> Unit
) : PagingDataAdapter<Chat, ChatsListAdapter.ChatViewHolder>(ChatDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
        holder.itemView.onClick(true) {
            onClick.invoke(item)
        }
    }

    override fun getItemViewType(position: Int) = R.layout.item_chat_list

    class ChatViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(chat: Chat) {
            binding.setVariable(BR.chat, chat)
            binding.executePendingBindings()
        }

    }

    object ChatDiff : DiffUtil.ItemCallback<Chat>() {

        override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
            return oldItem.lastMessage == newItem.lastMessage
                    && oldItem.name == newItem.name
                    && oldItem.photo == newItem.photo
                    && oldItem.updatedAt == newItem.updatedAt
                    && oldItem.unreadMessageCount == newItem.unreadMessageCount
        }

    }
}