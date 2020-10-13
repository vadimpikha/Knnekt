package knnekt.domain.messages

import knnekt.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class SendMessageUseCase(
    private val messagesRepository: MessagesRepository,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<SendMessageUseCase.Params, Unit>(dispatcher) {

    override suspend fun execute(parameters: Params) {
        messagesRepository.sendMessage(parameters.text, parameters.chatId)
    }

    data class Params(
        val text: String,
        val chatId: String
    )
}