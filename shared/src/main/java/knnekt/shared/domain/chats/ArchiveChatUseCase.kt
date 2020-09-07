package knnekt.shared.domain.chats

import knnekt.shared.data.chats.ChatsRepository
import knnekt.shared.domain.CoroutineUseCase
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