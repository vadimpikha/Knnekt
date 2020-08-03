package blasty.domain.usecase.base

import blasty.domain.dispatcher.DispatchersProvider
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
abstract class CoroutineUseCase<out ReturnType, in Params>(
    private val dispatcherProvider: DispatchersProvider
) {

    abstract suspend fun execute(params: Params = Unit as Params): ReturnType

    suspend operator fun invoke(
        params: Params = Unit as Params
    ): ReturnType = withContext(dispatcherProvider.io()) {
        execute(params)
    }
}