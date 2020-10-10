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
import knnekt.domain.entity.Chat
import knnekt.presentation.util.isArchivedSection

class ChatsListAdapter(
    private val onClick: (String) -> Unit
) : PagingDataAdapter<Chat, ChatsListAdapter.ChatViewHolder>(ChatDiff) {

    var tracker: SelectionTracker<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        ).apply {
            itemView.setOnClickListener {
                onClick.invoke(getItem(bindingAdapterPosition)?.id ?: return@setOnClickListener)
            }
            itemView.setOnLongClickListener {
               true
            }
        }
    }

    fun getItemAtPosition(position: Int) = getItem(position)
    fun getPositionOf(id: String) = snapshot().items.indexOfFirst { it.id == id }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = getItem(position)
        tracker?.let {
            holder.bind(item, it.isSelected(item?.id))
        } ?: run {
            holder.bind(item, false)
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

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<String>? {
            return object : ItemDetailsLookup.ItemDetails<String>() {
                override fun getPosition() = bindingAdapterPosition
                override fun getSelectionKey() = recentItem?.id
            }
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