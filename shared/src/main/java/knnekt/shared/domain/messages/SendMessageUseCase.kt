package knnekt.shared.domain.messages

import knnekt.shared.data.messages.MessagesRepository
import knnekt.shared.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    dispatcher: CoroutineDispatcher
): CoroutineUseCase<SendMessageUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(parameters: Params) {
        messagesRepository.sendMessage(parameters.text, parameters.chatId)
    }

    data class Params(val text: String, val chatId: String)
}