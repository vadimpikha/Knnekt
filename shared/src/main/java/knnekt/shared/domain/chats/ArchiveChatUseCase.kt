package knnekt.shared.domain.chats

import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.ConnectycubeRestChatService
import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class ArchiveChatUseCase(
    chatsRepository: ChatsRepository,
    dispatcher: CoroutineDispatcher
): CoroutineUseCase<Chat, Unit>(dispatcher) {

    override suspend fun execute(parameters: Chat) {

    }
}