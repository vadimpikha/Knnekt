package blasty.domain.usecase

import blasty.domain.datasource.local.LocalChatsDataSource
import blasty.domain.datasource.remote.RemoteChatsDataSource
import blasty.domain.dispatcher.DispatchersProvider
import blasty.domain.entity.internal.Resource
import blasty.domain.entity.local.Chat
import blasty.domain.repository.networkBoundResource
import blasty.domain.usecase.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

class FetchChatsUseCase(
    private val localChatsDataSource: LocalChatsDataSource,
    private val remoteChatsDataSource: RemoteChatsDataSource,
    dispatchers: DispatchersProvider
) : FlowUseCase<Resource<List<Chat>>, FetchChatsUseCase.Params>(
    dispatchers
) {

    override fun execute(params: Params): Flow<Resource<List<Chat>>> {
        return networkBoundResource(
            query = { localChatsDataSource.getChats(params.limit, params.skip) },
            fetch = { remoteChatsDataSource.getChats(params.limit, params.skip) },
            saveFetchResult = { chats ->  localChatsDataSource.saveChats(chats) }
        )
    }


    data class Params(val limit: Int, val skip: Int)

}