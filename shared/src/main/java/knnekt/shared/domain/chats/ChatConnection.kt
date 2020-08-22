package knnekt.shared.domain.chats

import knnekt.shared.result.Result

interface ChatConnection {

    suspend fun sendMessage(text: String): Result<Unit>


}