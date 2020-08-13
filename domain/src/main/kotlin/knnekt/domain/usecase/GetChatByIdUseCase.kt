package knnekt.domain.usecase

import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import knnekt.domain.repository.ChatsRepository
import knnekt.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class GetChatByIdUseCase(
    private val chatsRepository: ChatsRepository,
    dispatchers: DispatchersProvider
): FlowUseCase<Resource<Chat>, String>(dispatchers) {

    override fun execute(params: String): Flow<Resource<Chat>> {
        return chatsRepository.getChatById(params)
    }
}