package blasty.domain.usecase.base

import blasty.domain.dispatcher.DispatchersProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

@Suppress("UNCHECKED_CAST")
abstract class FlowUseCase<out ReturnType, in Params>(
    private val dispatchersProvider: DispatchersProvider
) {

    abstract fun execute(params: Params = Unit as Params): Flow<ReturnType>

    operator fun invoke(
        scope: CoroutineScope,
        params: Params = Unit as Params,
        onResult: (ReturnType) -> Unit = {}
    ) {
        scope.launch(dispatchersProvider.main()) {
            execute(params)
                .flowOn(dispatchersProvider.io())
                .collect { onResult(it) }
        }
    }
}