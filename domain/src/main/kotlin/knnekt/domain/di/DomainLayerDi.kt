package knnekt.domain.di

import knnekt.domain.usecase.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

object DomainLayerDi {

    val useCaseModule = Kodein.Module("useCaseModule") {
        bind<CheckUserSignedInUseCase>() with provider {
            CheckUserSignedInUseCase(instance())
        }

        bind<SendConfirmCodeUseCase>() with provider {
            SendConfirmCodeUseCase(instance(), instance())
        }

        bind<GetChatByIdUseCase>() with provider {
            GetChatByIdUseCase(instance(), instance())
        }

        bind<FirebaseSignInUseCase>() with provider {
            FirebaseSignInUseCase(instance(), instance())
        }

        bind<ConnectycubeSignInUseCase>() with provider {
            ConnectycubeSignInUseCase(instance(), instance(), instance())
        }
        bind<FetchChatsUseCase>() with provider {
            FetchChatsUseCase(instance(), instance())
        }
    }

}