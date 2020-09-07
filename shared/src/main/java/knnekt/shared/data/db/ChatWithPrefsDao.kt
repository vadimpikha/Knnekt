package knnekt.shared.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query

@Dao
interface ChatWithPrefsDao {

    @Query("SELECT * FROM chats ORDER BY lastMessageDateSent DESC")
    fun getChatsPaging(): PagingSource<Int, ChatWithPrefsEntity>

}