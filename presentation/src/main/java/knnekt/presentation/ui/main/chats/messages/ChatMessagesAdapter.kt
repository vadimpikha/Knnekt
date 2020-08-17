package knnekt.presentation.ui.main.chats.messages

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import knnekt.R
import knnekt.domain.repository.LocalPreferencesRepository
import knnekt.presentation.entity.MessageItem
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message_simple_ougoing.*

class ChatMessagesAdapter(
    val prefs: LocalPreferencesRepository
) : ListAdapter<MessageItem, ChatMessagesAdapter.ChatMessageViewHolder>(MessageDiff) {

    companion object {
        const val TYPE_INCOMING_SIMPLE = 0
        const val TYPE_OUTGOING_SIMPLE = 1
    }

    val userId by lazy { prefs.userId }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_OUTGOING_SIMPLE -> SimpleOutGoingMessageViewHolder(
                inflater.inflate(R.layout.item_message_simple_ougoing, parent, false)
            )
            TYPE_INCOMING_SIMPLE -> SimpleIncomingMessageViewHolder(
                inflater.inflate(R.layout.item_message_simple_incoming, parent, false)
            )
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: ChatMessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)

        return when {
            isIncoming(item) -> TYPE_INCOMING_SIMPLE
            else -> TYPE_OUTGOING_SIMPLE
        }
    }

    private fun isIncoming(message: MessageItem): Boolean {
        return message.senderId != null && message.senderId != userId
    }

    abstract class ChatMessageViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        open fun bind(message: MessageItem) {
            message_time.text = formatDate(message.dateSent)
        }

        protected open fun formatDate(seconds: Long): String = DateUtils.formatDateTime(
            containerView.context,
            seconds * 1000L,
            DateUtils.FORMAT_SHOW_TIME
        )

    }

    class SimpleIncomingMessageViewHolder(itemView: View) : ChatMessageViewHolder(itemView) {
        override fun bind(message: MessageItem) {
            super.bind(message)
            message_body.text = message.body
        }
    }

    class SimpleOutGoingMessageViewHolder(itemView: View) : ChatMessageViewHolder(itemView) {
        override fun bind(message: MessageItem) {
            super.bind(message)
            message_body.text = message.body
        }
    }

    object MessageDiff : DiffUtil.ItemCallback<MessageItem>() {

        override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
            return oldItem == newItem
        }

    }

}