package blasty.data.datasource.remote

import blasty.domain.converters.convertToChat
import blasty.domain.datasource.remote.RemoteChatsDataSource
import blasty.domain.entity.local.Chat
import blasty.utils.await
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.core.request.RequestGetBuilder

class RemoteChatsDataSourceImpl : RemoteChatsDataSource {

    override suspend fun getChats(limit: Int, skip: Int): List<Chat> {
        val request = RequestGetBuilder().apply {
            this.limit = limit
            this.skip = skip
        }
       val (dialogs, _) = ConnectycubeRestChatService.getChatDialogs(null, request).await()
        return dialogs.map { convertToChat(it) }
    }
}