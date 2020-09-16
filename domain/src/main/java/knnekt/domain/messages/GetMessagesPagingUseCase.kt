package knnekt.domain.messages

import androidx.paging.PagingData
import knnekt.domain.usecase.UseCase
import knnekt.domain.entity.Message
import kotlinx.coroutines.flow.Flow

class GetMessagesPagingUseCase(
    private val messagesRepository: MessagesRepository
) : UseCase<String, Flow<PagingData<Message>>>() {

    override fun execute(parameters: String): Flow<PagingData<Message>> {
        return messagesRepository.getMessagesPagingData(parameters)
    }
}