package knnekt.data.datasource.remote

import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.core.request.RequestGetBuilder
import knnekt.data.mapper.Mapper
import knnekt.data.util.await
import knnekt.domain.datasource.remote.RemoteChatsDataSource
import knnekt.domain.entity.Chat

class RemoteChatsDataSourceImpl(
    private val mapper: Mapper<ConnectycubeChatDialog, Chat>
) : RemoteChatsDataSource {

    override suspend fun getChats(limit: Int, skip: Int): List<Chat> {
        val request = RequestGetBuilder().apply {
            this.limit = limit
            this.skip = skip
        }
        val (dialogs, _) = ConnectycubeRestChatService.getChatDialogs(null, request).await()
        return dialogs.map(mapper::convert)
    }
}