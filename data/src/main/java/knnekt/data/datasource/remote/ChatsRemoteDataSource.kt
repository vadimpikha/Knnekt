package knnekt.data.datasource.remote

import knnekt.data.datasource.remote.entity.ChatRemoteEntity
import knnekt.data.datasource.remote.entity.MessageRemoteEntity

interface ChatsRemoteDataSource {

    suspend fun getChats(): List<ChatRemoteEntity>

}