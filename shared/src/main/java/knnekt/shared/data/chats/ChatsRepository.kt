package knnekt.shared.data.chats

import androidx.paging.*
import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import knnekt.shared.data.db.ChatDao
import knnekt.shared.data.db.ChatEntity
import knnekt.shared.data.entity.Chat
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await
import knnekt.shared.result.Result
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {

    @OptIn(ExperimentalPagingApi::class)
    fun getChatsPagingData(): Flow<PagingData<ChatEntity>>
    suspend fun updateChat(chatId: String)

}

class ChatsRepositoryImpl(
    private val chatDao: ChatDao,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatDialog, ChatEntity>
) : ChatsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getChatsPagingData(): Flow<PagingData<ChatEntity>> {
        return Pager(
            config = PagingConfig(15),
            remoteMediator = ChatRemoteMediator(chatDao, remoteToEntityMapper),
            pagingSourceFactory = { chatDao.getChatsPaging() }
        ).flow
    }

    override suspend fun updateChat(chatId: String) {
        chatDao.getChatsPaging().invalidate()


//        val (dialog, _) = ConnectycubeRestChatService.getChatDialogById(chatId).await()
//        val existsInCache = chatDao.getChat(chatId) != null
//        val chat = remoteToEntityMapper.convert(dialog)
//        if (existsInCache)
//            chatDao.update(chat)
//        else
//            chatDao.insert(chat)
    }

    //    override suspend fun getChatById(id: String): Result<Chat> {
//        return Result.catch { chatDao.getChat(id) }
//    }
}