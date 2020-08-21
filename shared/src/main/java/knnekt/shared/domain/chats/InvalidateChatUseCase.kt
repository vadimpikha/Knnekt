package knnekt.shared.domain.chats

import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class InvalidateChatUseCase(
    private val chatsRepository: ChatsRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String) {
        chatsRepository.updateChat(parameters)
    }
}