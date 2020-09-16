package knnekt.data.datasource.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import knnekt.data.datasource.db.entity.ChatWithPrefsEntity

@Dao
interface ChatWithPrefsDao {

    @Transaction
    @Query("SELECT * FROM chats ORDER BY last_message_date_sent DESC")
    fun getChatsPaging(): PagingSource<Int, ChatWithPrefsEntity>

}