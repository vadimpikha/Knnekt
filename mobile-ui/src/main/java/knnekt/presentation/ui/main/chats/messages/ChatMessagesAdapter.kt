package knnekt.presentation.ui.main.chats.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import knnekt.BR
import knnekt.R
import knnekt.domain.entity.Message

class ChatMessagesAdapter : PagingDataAdapter<Message, ChatMessagesAdapter.ChatMessageViewHolder>(MessageDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        return ChatMessageViewHolder.Factory(parent, viewType)
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val item = getItem(position)
        holder.bindTo(item)
    }

    override fun onViewRecycled(holder: ChatMessageViewHolder) {
        holder.recycle()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position) ?: return -1

        val incoming = item.isIncoming
        val attachment = withAttachment(item)

        return when {
            incoming && attachment -> R.layout.item_message_image_incoming
            incoming && !attachment -> R.layout.item_message_simple_incoming
            !incoming && attachment -> R.layout.item_message_image_outgoing
            else -> R.layout.item_message_simple_ougoing
        }
    }

    private fun withAttachment(message: Message): Boolean {
        return message.attachments.isNotEmpty()
    }

    class ChatMessageViewHolder(binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {

        private var binding: ViewDataBinding? = binding

        fun bindTo(message: Message?) {
            binding?.setVariable(BR.message, message)
            binding?.executePendingBindings()
        }

        fun recycle() {
            binding = null
        }

        companion object {
            @Suppress("FunctionName")
            fun Factory(
                parent: ViewGroup,
                viewType: Int
            ): ChatMessageViewHolder {
                require(viewType > 0)
                val inflater = LayoutInflater.from(parent.context)
                return ChatMessageViewHolder(
                    DataBindingUtil.inflate(inflater, viewType, parent, false)
                )
            }
        }

    }

    object MessageDiff : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }

    }

}