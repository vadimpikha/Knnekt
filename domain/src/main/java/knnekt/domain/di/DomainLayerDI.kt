package knnekt.domain.di

import knnekt.domain.chats.ArchiveChatUseCase
import knnekt.domain.chats.GetChatByIdUseCase
import knnekt.domain.chats.GetChatsPagingUseCase
import knnekt.domain.chats.InvalidateChatUseCase
import knnekt.domain.messages.GetMessagesPagingUseCase
import knnekt.domain.messages.SendMessageUseCase
import knnekt.domain.users.CheckUserSignedInUseCase
import knnekt.domain.users.SignInUseCase
import kotlinx.coroutines.Dispatchers
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object DomainLayerDI {

    val useCaseModule = DI.Module("DomainLayer.UseCaseModule") {

        bind() from singleton {
            GetChatByIdUseCase(instance(), Dispatchers.IO)
        }

        bind() from singleton {
            CheckUserSignedInUseCase(instance())
        }

        bind() from  singleton {
            SignInUseCase(instance(), Dispatchers.Main)
        }

        bind() from  singleton {
            GetChatsPagingUseCase(instance())
        }

        bind() from  singleton {
            InvalidateChatUseCase(instance(), Dispatchers.Main)
        }

        bind() from singleton {
            GetMessagesPagingUseCase(instance())
        }

        bind() from singleton {
            SendMessageUseCase(instance(), Dispatchers.Main)
        }

        bind() from singleton {
            ArchiveChatUseCase(instance(), Dispatchers.Default)
        }

    }

}