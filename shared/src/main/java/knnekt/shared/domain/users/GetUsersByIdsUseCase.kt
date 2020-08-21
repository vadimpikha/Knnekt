package knnekt.shared.domain.users

//import knnekt.domain.dispatcher.DispatchersProvider
//import knnekt.domain.entity.User
//import knnekt.domain.entity.internal.Resource
//import knnekt.domain.repository.UsersRepository
//import knnekt.domain.usecase.base.FlowUseCase
//import knnekt.shared.data.db.UserEntity
//import knnekt.shared.data.users.UsersRepository
//import knnekt.shared.domain.CoroutineUseCase
//import kotlinx.coroutines.CoroutineDispatcher
//
//class GetUsersByIdsUseCase(
//    private val usersRepository: UsersRepository,
//    dispatcher: CoroutineDispatcher
//): CoroutineUseCase<IntArray, List<UserEntity>>(dispatcher) {
//
//    override suspend fun execute(parameters: IntArray): List<UserEntity> {
//        return usersRepository.getUsersByIds(*params)
//    }
//}