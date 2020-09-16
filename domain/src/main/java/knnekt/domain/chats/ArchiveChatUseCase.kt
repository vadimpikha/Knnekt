package knnekt.domain.chats

import knnekt.domain.usecase.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher

class ArchiveChatUseCase(
    private val chatsRepository: ChatsRepository,
    dispatcher: CoroutineDispatcher
): CoroutineUseCase<ArchiveChatUseCase.Param, Unit>(dispatcher) {


    override suspend fun execute(parameters: Param) {
        chatsRepository.archiveChat(parameters.chatId, parameters.archive)
    }

    data class Param(val chatId: String, val archive: Boolean)

}