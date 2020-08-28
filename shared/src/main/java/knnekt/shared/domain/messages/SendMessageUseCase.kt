package knnekt.shared.domain.messages

import knnekt.shared.data.messages.MessagesRepository
import knnekt.shared.domain.CoroutineUseCase

class SendMessageUseCase(
    private val messagesRepository: MessagesRepository
): CoroutineUseCase<SendMessageUseCase.Params, Unit>(null) {

    override suspend fun execute(parameters: Params) {
        messagesRepository.sendMessage(parameters.text, parameters.chatId)
    }

    data class Params(val text: String, val chatId: String)
}