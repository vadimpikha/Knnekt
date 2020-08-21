package knnekt.shared.domain

/**
 * Executes business logic synchronously.
 */
abstract class UseCase<in P, R> {

    operator fun invoke(parameters: P): R {
        return execute(parameters)
    }

    /**
     * Override this to set the code to be executed.
     */
    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): R
}
