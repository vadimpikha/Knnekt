package knnekt.presentation.ui.main.chats.messages

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import knnekt.R
import knnekt.shared.data.entity.Message
import knnekt.presentation.util.dp
import knnekt.shared.domain.repository.LocalPreferencesRepository
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_message_image_incoming.*
import kotlinx.android.synthetic.main.item_message_simple_ougoing.*
import kotlinx.android.synthetic.main.item_message_simple_ougoing.message_time

class ChatMessagesAdapter : PagingDataAdapter<Message, ChatMessagesAdapter.ChatMessageViewHolder>(MessageDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(viewType, parent, false)

        return when (viewType) {
            R.layout.item_message_simple_ougoing -> TextMessageViewHolder(view)
            R.layout.item_message_simple_incoming -> TextMessageViewHolder(view)
            R.layout.item_message_image_incoming -> ImageMessageViewHolder(view)
            R.layout.item_message_image_outgoing -> ImageMessageViewHolder(view)
            else -> throw IllegalStateException()
        }
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

    abstract class ChatMessageViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        open fun bind(message: Message) {
            message_time.text = formatDate(message.dateSent)
        }

        protected open fun formatDate(seconds: Long): String = DateUtils.formatDateTime(
            containerView.context,
            seconds * 1000L,
            DateUtils.FORMAT_SHOW_TIME
        )

    }

    class ImageMessageViewHolder(itemView: View) : ChatMessageViewHolder(itemView) {
        override fun bind(message: Message) {
            super.bind(message)
            val width = itemView.context.dp(200)
            val height = itemView.context.dp(300)

            Glide.with(itemView)
                .load(message.attachments?.firstOrNull()?.url)
                .placeholder(R.drawable.image_placeholder)
                .override(width, height)
                .dontTransform()
                .error(R.drawable.image_error_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(attachment_image)
        }
    }

    class TextMessageViewHolder(itemView: View) : ChatMessageViewHolder(itemView) {
        override fun bind(message: Message) {
            super.bind(message)
            message_body.text = message.body
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