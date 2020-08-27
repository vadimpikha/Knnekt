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
import knnekt.shared.data.entity.Message

class ChatMessagesAdapter :
    PagingDataAdapter<Message, ChatMessagesAdapter.ChatMessageViewHolder>(MessageDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ChatMessageViewHolder(
            DataBindingUtil.inflate(inflater, viewType, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        val item = getItem(position) ?: return
        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)!!

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
        return !message.attachments.isNullOrEmpty()
    }

    class ChatMessageViewHolder(val binding: ViewDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: Message) {
            binding.setVariable(BR.message, message)
            binding.executePendingBindings()
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