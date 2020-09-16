package knnekt.domain.chats

import knnekt.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class InvalidateChatUseCase(
    private val chatsRepository: ChatsRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Unit>(dispatcher) {

    override suspend fun execute(parameters: String) {
        chatsRepository.updateChat(parameters)
    }
}