package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeAttachment
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.chat.request.MessageGetBuilder
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.MessageEntity
import knnekt.data.datasource.remote.entity.AttachmentRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.data.util.await
import knnekt.domain.mapper.Mapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MessagesRemoteDataSource {

    suspend fun createMessage(chatId: String, text: String, userId: Int): MessageRemoteEntity
    suspend fun sendMessage(message: MessageRemoteEntity): MessageRemoteEntity
    suspend fun getRecentMessages(chatId: String, limit: Int): List<MessageRemoteEntity>
    suspend fun getMessagesAfter(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>
    suspend fun getMessagesBefore(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>

}

class MessagesRemoteDataSourceImpl : MessagesRemoteDataSource {

    override suspend fun createMessage(
        chatId: String,
        text: String,
        userId: Int
    ): MessageRemoteEntity {
        return ConnectycubeChatMessage().apply {
            body = text
            dialogId = chatId
            dateSent = System.currentTimeMillis() / 1000
            senderId = userId
        }.let(::convert)
    }

    override suspend fun sendMessage(message: MessageRemoteEntity): MessageRemoteEntity {
        val connectycubeChatMessage = ConnectycubeChatMessage().apply {
            body = message.body
            dialogId = message.chatId
            dateSent = message.dateSend
            senderId = message.senderId
        }

        return ConnectycubeRestChatService.createMessage(connectycubeChatMessage, true)
            .await().let(::convert)
    }

    override suspend fun getRecentMessages(
        chatId: String,
        limit: Int
    ): List<MessageRemoteEntity> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await().map(this::convert)
    }

    override suspend fun getMessagesAfter(
        chatId: String,
        limit: Int,
        date: Long
    ): List<MessageRemoteEntity> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            gt("date_sent", date)
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await().map(this::convert)
    }

    override suspend fun getMessagesBefore(
        chatId: String,
        limit: Int,
        date: Long
    ): List<MessageRemoteEntity> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            lt("date_sent", date)
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await().map(this::convert)
    }


    private fun convert(message: ConnectycubeChatMessage): MessageRemoteEntity {
        return MessageRemoteEntity(
            id = message.id,
            chatId = message.dialogId,
            dateSend = message.dateSent,
            body = message.body,
            readIds = message.readIds?.toList().orEmpty(),
            deliveredIds = message.deliveredIds?.toList().orEmpty(),
            senderId = message.senderId,
            attachments = message.attachments?.map(this::convert).orEmpty(),
            recipientId = message.recipientId,
            markable = message.isMarkable,
            viewsCount = message.viewsCount
        )
    }

    private fun convert(attachment: ConnectycubeAttachment): AttachmentRemoteEntity {
        return AttachmentRemoteEntity(
            id = attachment.id,
            type = attachment.type,
            url = attachment.url
        )
    }

}