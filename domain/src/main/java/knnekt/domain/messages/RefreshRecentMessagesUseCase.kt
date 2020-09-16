package knnekt.domain.messages

import knnekt.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class RefreshRecentMessagesUseCase (
    private val messagesRepository: MessagesRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<RefreshRecentMessagesUseCase.Param, Unit>(dispatcher) {

    override suspend fun execute(parameters: Param) {
        messagesRepository.refreshRecentMessages(parameters.chatId)
    }

    data class Param(val chatId: String, val count: Int)
}