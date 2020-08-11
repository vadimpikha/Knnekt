package knnekt.domain.datasource.remote

import knnekt.domain.entity.Chat

interface RemoteChatsDataSource {

    suspend fun getChats(limit: Int, skip: Int): List<Chat>

}