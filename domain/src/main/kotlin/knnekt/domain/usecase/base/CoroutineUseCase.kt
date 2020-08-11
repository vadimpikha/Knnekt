package knnekt.domain.usecase.base

import knnekt.domain.dispatcher.DispatchersProvider
import kotlinx.coroutines.withContext

@Suppress("UNCHECKED_CAST")
abstract class CoroutineUseCase<out ReturnType, in Params>(
    private val dispatcherProvider: knnekt.domain.dispatcher.DispatchersProvider
) {

    abstract suspend fun execute(params: Params = Unit as Params): ReturnType

    suspend operator fun invoke(
        params: Params = Unit as Params
    ): ReturnType = withContext(dispatcherProvider.io()) {
        execute(params)
    }
}