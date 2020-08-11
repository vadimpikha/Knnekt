package knnekt.domain.usecase

import knnekt.domain.dispatcher.DispatchersProvider
import knnekt.domain.entity.Chat
import knnekt.domain.entity.internal.Resource
import knnekt.domain.repository.ChatsRepository
import knnekt.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class FetchChatsUseCase(
    private val chatsRepository: ChatsRepository,
    dispatchers: DispatchersProvider
) : FlowUseCase<Resource<List<Chat>>, FetchChatsUseCase.Params>(
    dispatchers
) {

    override fun execute(params: Params): Flow<Resource<List<Chat>>> {
        return chatsRepository.getChats(params.limit, params.skip)
    }


    data class Params(val limit: Int, val skip: Int)

}