package knnekt.shared.data.chats

import android.util.Log
import androidx.paging.*
import androidx.room.withTransaction
import com.connectycube.chat.ConnectycubeRestChatService
import com.connectycube.chat.Consts
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.request.MessageGetBuilder
import com.connectycube.core.request.RequestGetBuilder
import knnekt.shared.data.db.*
import knnekt.shared.data.entity.Chat
import knnekt.shared.data.mapper.Mapper
import knnekt.shared.data.util.await
import knnekt.shared.utils.processScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

interface ChatsRepository {

    @OptIn(ExperimentalPagingApi::class)
    fun getChatsPagingData(): Flow<PagingData<ChatWithPrefsEntity>>
    suspend fun updateChat(chatId: String)
    suspend fun archiveChat(chatId: String, archive: Boolean)

}

class ChatsRepositoryImpl(
    private val db: AppDatabase,
    private val remoteToEntityMapper: Mapper<ConnectycubeChatDialog, ChatEntity>
) : ChatsRepository {

    private val archivedSectionItem = ChatEntity(Chat.ARCHIVED_CHAT_ID, 1).apply {
        setOccupantsIds(emptyList())
        name = "Archived"
        lastMessageDateSent = Long.MAX_VALUE
    }

    init {
        processScope.launch(Dispatchers.Default) {
            db.chatPrefsDao()
                .archivedChatsCount()
                .distinctUntilChanged()
                .collectLatest { count ->
                if (count > 0) {
                    db.chatDao().insertIfAbsent(archivedSectionItem)
                } else {
                    db.chatDao().deleteChatsByIds(Chat.ARCHIVED_CHAT_ID)
                }
            }
        }
    }

    override suspend fun archiveChat(chatId: String, archive: Boolean) {
        db.withTransaction {
            val chatPrefsDao = db.chatPrefsDao()
            val pref = chatPrefsDao.findPref(chatId)
            if (pref != null) {
                chatPrefsDao.insert(pref.copy(isArchived = archive))
            } else {
                chatPrefsDao.insert(ChatPrefsEntity(chatId, false, archive))
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getChatsPagingData(): Flow<PagingData<ChatWithPrefsEntity>> {
        return Pager(
            config = PagingConfig(15, enablePlaceholders = false),
            remoteMediator = ChatRemoteMediator(db.chatDao(), remoteToEntityMapper),
            pagingSourceFactory = { db.chatsWithPrefsDao().getChatsPaging() },
        ).flow
    }

    override suspend fun updateChat(chatId: String) {
        val request = MessageGetBuilder()
            .markAsRead(false)
            .eq(Consts.DIALOG_ID_FIELD_NAME, chatId)


        val dialogs = ConnectycubeRestChatService.getChatDialogs(null, request).await()
        val chat = remoteToEntityMapper.convert(dialogs.single())
        db.chatDao().upsert(chat)
        Log.d("ChatsRepository", "Updated $chat")
    }

    //    override suspend fun getChatById(id: String): Result<Chat> {
//        return Result.catch { chatDao.getChat(id) }
//    }
}