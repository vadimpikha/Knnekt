package knnekt.shared.domain.di

import knnekt.shared.data.mapper.ChatMapper
import knnekt.shared.data.mapper.MessageMapper
import knnekt.shared.data.mapper.UserMapper
import knnekt.shared.domain.chats.ArchiveChatUseCase
import knnekt.shared.domain.chats.GetChatsPagingUseCase
import knnekt.shared.domain.chats.InvalidateChatUseCase
import knnekt.shared.domain.messages.GetMessagesPagingUseCase
import knnekt.shared.domain.messages.SendMessageUseCase
import knnekt.shared.domain.repository.LocalPreferencesRepository
import knnekt.shared.domain.usecase.ConnectycubeSignInUseCase
import knnekt.shared.domain.users.CheckUserSignedInUseCase
import kotlinx.coroutines.Dispatchers
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object DomainLayerDi {

    val useCaseModule = Kodein.Module("useCaseModule") {

        bind<CheckUserSignedInUseCase>() with provider {
            CheckUserSignedInUseCase(instance())
        }

        bind<ConnectycubeSignInUseCase>() with provider {
            ConnectycubeSignInUseCase(instance(), instance(), UserMapper, Dispatchers.Main)
        }

        bind<GetChatsPagingUseCase>() with provider {
            GetChatsPagingUseCase(instance(), ChatMapper)
        }

        bind<InvalidateChatUseCase>() with provider {
            InvalidateChatUseCase(instance(), Dispatchers.Main)
        }

        bind() from provider {
            val mapper = MessageMapper(instance<LocalPreferencesRepository>().user!!.id)
            GetMessagesPagingUseCase(instance(), mapper)
        }

        bind() from provider {
            SendMessageUseCase(instance(), Dispatchers.Main)
        }

        bind() from provider {
            ArchiveChatUseCase(instance(), Dispatchers.Default)
        }

    }

}