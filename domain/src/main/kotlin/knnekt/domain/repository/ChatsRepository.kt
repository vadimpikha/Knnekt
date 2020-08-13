package knnekt.domain.repository


import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import kotlinx.coroutines.flow.Flow

interface ChatsRepository {

    fun getChats(limit: Int, skip: Int): Flow<Resource<List<Chat>>>

    fun getChatById(id: String): Flow<Resource<Chat>>

}