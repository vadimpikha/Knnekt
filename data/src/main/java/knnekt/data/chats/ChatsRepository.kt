package knnekt.data.chats

import androidx.paging.*
import androidx.room.withTransaction
import knnekt.data.datasource.db.AppDatabase
import knnekt.data.datasource.db.entity.ChatEntity
import knnekt.data.datasource.db.entity.ChatPrefsEntity
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity
import knnekt.data.datasource.remote.ChatsRemoteDataSource
import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.data.util.processScope
import knnekt.domain.chats.ChatsRepository
import knnekt.domain.entity.Chat
import knnekt.domain.mapper.Mapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ChatsRepositoryImpl(
    private val db: AppDatabase,
    private val chatsRemoteDataSource: ChatsRemoteDataSource,
    private val remoteToEntityMapper: Mapper<ChatRemoteEntity, ChatEntity>,
    private val entityToPresentationMapper: Mapper<ChatWithPrefsEntity, Chat>
) : ChatsRepository {

    init {
        processScope.launch(Dispatchers.Default) {
            db.chatDao()
                .getArchivedChats()
                .distinctUntilChanged()
                .collectLatest { archivedChats ->
                    if (archivedChats.isNotEmpty()) {
                        val chatNames = archivedChats.joinToString { it.name }
                        val unreadChats = archivedChats.count { it.unreadMessageCount > 0 }
                        db.chatDao()
                            .insert(ChatEntity.createArchivedChatsSection(chatNames, unreadChats))
                    } else {
                        db.chatDao().deleteChatsByIds(ChatEntity.ARCHIVED_SECTION_ID)
                    }
                }
        }
    }

    override fun getChatById(chatId: String): Flow<Chat> {
        return db.chatsWithPrefsDao().getChat(chatId)
            .map { entityToPresentationMapper.convert(it) }
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
    override fun getChatsPagingData(): Flow<PagingData<Chat>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = ChatRemoteMediator(db, chatsRemoteDataSource, remoteToEntityMapper),
            pagingSourceFactory = { db.chatsWithPrefsDao().getChatsPaging() },
        ).flow.map { data ->
            data.map(entityToPresentationMapper::convert)
        }
    }

    override suspend fun updateChat(chatId: String) {
        val chat = chatsRemoteDataSource.getChatById(chatId)
            .let(remoteToEntityMapper::convert)

        db.chatDao().insert(chat)
    }
}