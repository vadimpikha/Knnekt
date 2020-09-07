package knnekt.shared.data.chats

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeChatMessage
import com.connectycube.chat.request.MessageGetBuilder
import knnekt.shared.data.util.await

interface ChatMessagesRemoteSource {

    suspend fun getTop(chatId: String, limit: Int): List<ConnectycubeChatMessage>

    suspend fun getTopBefore(
        chatId: String,
        limit: Int,
        before: Long
    ): List<ConnectycubeChatMessage>

    suspend fun getTopAfter(chatId: String, limit: Int, after: Long): List<ConnectycubeChatMessage>

}

class ChatMessagesRemoteSourceImpl : ChatMessagesRemoteSource {

    override suspend fun getTop(chatId: String, limit: Int): List<ConnectycubeChatMessage> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await()
    }

    override suspend fun getTopBefore(
        chatId: String,
        limit: Int,
        before: Long
    ): List<ConnectycubeChatMessage> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            lt("date_sent", before)
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await()
    }

    override suspend fun getTopAfter(
        chatId: String,
        limit: Int,
        after: Long
    ): List<ConnectycubeChatMessage> {
        val request = MessageGetBuilder().apply {
            this.limit = limit
            sortDesc("date_sent")
            gt("date_sent", after)
            markAsRead(false)
        }

        return ConnectycubeRestChatService.getDialogMessages(
            ConnectycubeChatDialog(chatId),
            request
        ).await()
    }


}
