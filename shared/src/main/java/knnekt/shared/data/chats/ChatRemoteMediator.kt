package knnekt.shared.data.chats

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.core.request.RequestGetBuilder
import knnekt.shared.data.db.ChatDao
import knnekt.shared.data.db.ChatEntity
import knnekt.shared.data.db.ChatWithPrefsEntity
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await

@OptIn(ExperimentalPagingApi::class)
class ChatRemoteMediator(
    private val chatDao: ChatDao,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatDialog, ChatEntity>
) : RemoteMediator<Int, ChatWithPrefsEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ChatWithPrefsEntity>
    ): MediatorResult {

        try {
            val allDataSize = state.pages.sumBy { it.data.size }

            val request = RequestGetBuilder().apply {
                limit = state.config.pageSize
                skip = allDataSize
            }

            val dialogs = ConnectycubeRestChatService.getChatDialogs(null, request).await()

            val chats = dialogs.map(remoteToEntityMapper::convert)
            chatDao.insertAll(chats)

            return MediatorResult.Success(endOfPaginationReached = dialogs.isEmpty())
        } catch (exception: Exception) {
            return MediatorResult.Error(exception)
        }

    }


}