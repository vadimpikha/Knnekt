package knnekt.shared.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface ChatWithPrefsDao {

    @Transaction
    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    fun getChatsPaging(): PagingSource<Int, ChatWithPrefsEntity>

}