package knnekt.domain.chats

import knnekt.domain.entity.Chat
import knnekt.domain.usecase.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow

class GetChatByIdUseCase(
    private val chatsRepository: ChatsRepository,
    dispatcher: CoroutineDispatcher
): FlowUseCase<String, Chat>(dispatcher) {

    override fun execute(parameters: String): Flow<Chat> {
        return chatsRepository.getChatById(parameters)
    }
}