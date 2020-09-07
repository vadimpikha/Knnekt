package knnekt.shared.data.chats

import android.util.Log
import androidx.paging.*
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.Consts
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.request.MessageGetBuilder
import com.connectycube.core.request.RequestGetBuilder
import knnekt.shared.data.db.ChatDao
import knnekt.shared.data.db.ChatEntity
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await
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
        val request = MessageGetBuilder()
            .markAsRead(false)
            .eq(Consts.DIALOG_ID_FIELD_NAME, chatId)


        val dialogs = ConnectycubeRestChatService.getChatDialogs(null, request).await()
        val chat = remoteToEntityMapper.convert(dialogs.single())
        chatDao.upsert(chat)
    }

    //    override suspend fun getChatById(id: String): Result<Chat> {
//        return Result.catch { chatDao.getChat(id) }
//    }
}