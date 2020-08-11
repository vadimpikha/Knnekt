package knnekt.domain.usecase.base

@Suppress("UNCHECKED_CAST")
abstract class UseCase<out ReturnType, in Params> {

    abstract fun execute(params: Params = Unit as Params): ReturnType

    operator fun invoke(params: Params = Unit as Params) = execute(params)

}