package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeAttachment
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.chat.request.MessageGetBuilder
import knnekt.data.datasource.remote.entity.AttachmentRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.data.util.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface MessagesRemoteDataSource {

    fun createMessage(chatId: String, text: String, userId: Int): Flow<MessageRemoteEntity>
    suspend fun getRecentMessages(chatId: String, limit: Int): List<MessageRemoteEntity>
    suspend fun getMessagesAfter(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>
    suspend fun getMessagesBefore(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>

}

class MessagesRemoteDataSourceImpl : MessagesRemoteDataSource {

    override fun createMessage(
        chatId: String,
        text: String,
        userId: Int
    ): Flow<MessageRemoteEntity> = flow {

        val message = ConnectycubeChatMessage().apply {
            body = text
            dialogId = chatId
            dateSent = System.currentTimeMillis() / 1000
            senderId = userId
        }
        emit(message.let(::convert))
        val sentMessage =  ConnectycubeRestChatService.createMessage(message, true)
            .await().let(::convert)

        emit(sentMessage)
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
            message.id,
            message.dialogId,
            message.dateSent,
            message.body,
            message.readIds?.toList().orEmpty(),
            message.deliveredIds?.toList().orEmpty(),
            message.senderId,
            message.attachments?.map(this::convert).orEmpty()
        )
    }

    private fun convert(attachment: ConnectycubeAttachment): AttachmentRemoteEntity {
        return AttachmentRemoteEntity(
            attachment.id,
            attachment.type,
            attachment.url
        )
    }

}