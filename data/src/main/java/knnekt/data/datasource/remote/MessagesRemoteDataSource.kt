package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeAttachment
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.chat.model.ConnectycubeDialogType
import com.connectycube.chat.request.MessageGetBuilder
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.remote.entity.AttachmentRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity
import knnekt.data.util.await

interface MessagesRemoteDataSource {

    fun obtainMessageId(): String
    suspend fun sendMessage(
        messageId: String,
        text: String,
        chatId: String,
        dateSend: Long,
        userId: Int
    )

    suspend fun getRecentMessages(chatId: String, limit: Int): List<MessageRemoteEntity>
    suspend fun getMessagesAfter(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>
    suspend fun getMessagesBefore(chatId: String, limit: Int, date: Long): List<MessageRemoteEntity>

}

class MessagesRemoteDataSourceImpl(
    private val db: AppDatabase
) : MessagesRemoteDataSource {

    private val chatsCache = HashMap<Pair<String, Int>, ConnectycubeChatDialog>()

    override fun obtainMessageId(): String {
        return ConnectycubeChatMessage().id
    }

    override suspend fun sendMessage(
        messageId: String,
        text: String,
        chatId: String,
        dateSend: Long,
        userId: Int
    ) {

        val dialog = obtainChat(chatId, userId)

        val connectycubeChatMessage = ConnectycubeChatMessage().apply {
            id = messageId
            body = text
            dialogId = chatId
            dateSent = dateSend
            senderId = userId
            isMarkable = true
            setSaveToHistory(true)
            if (dialog.isPrivate) this.recipientId = dialog.recipientId
        }

        dialog.sendMessage(connectycubeChatMessage)
    }

    private suspend fun obtainChat(chatId: String, userId: Int): ConnectycubeChatDialog {

        return chatsCache.getOrPut(chatId to userId) {

            val chat = db.chatDao().getChat(chatId)

            ConnectycubeChatDialog(chatId).apply {
                setOccupantsIds(chat.occupants)
                type = ConnectycubeDialogType.parseByCode(chat.dialogType)
                this.userId = userId
            }.also {
                it.initForChat(ConnectycubeChatService.getInstance())
            }
        }
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