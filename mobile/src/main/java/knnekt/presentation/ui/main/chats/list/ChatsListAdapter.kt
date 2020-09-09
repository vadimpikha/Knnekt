package knnekt.presentation.ui.main.chats.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import knnekt.BR
import knnekt.R
import knnekt.presentation.util.isArchivedSection
import knnekt.shared.data.entity.Chat

class ChatsListAdapter(
    private val onClick: (Chat) -> Unit
) : PagingDataAdapter<Chat, ChatsListAdapter.ChatViewHolder>(ChatDiff) {

//    var tracker: SelectionTracker<Chat>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    fun getItemAtPosition(position: Int) = getItem(position)
    fun getPosition(chat: Chat) = snapshot().items.indexOf(chat)

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position)

        /*tracker?.let {
            holder.bind(item, it.isSelected(item))
        } ?:*/ holder.bind(item, false)

        holder.itemView.setOnClickListener {
            onClick.invoke(getItem(holder.bindingAdapterPosition) ?: return@setOnClickListener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position)?.isArchivedSection == true)
            R.layout.item_archived_chats
        else
            R.layout.item_chat_list
    }

    class ChatViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        var recentItem: Chat? = null

        fun bind(chat: Chat?, isSelected: Boolean) {
            recentItem = chat
            binding.setVariable(BR.selected, isSelected)
            binding.setVariable(BR.chat, chat)
            binding.executePendingBindings()
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Chat> =
            object : ItemDetailsLookup.ItemDetails<Chat>() {
                override fun getPosition(): Int = bindingAdapterPosition
                override fun getSelectionKey(): Chat? = recentItem
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