package knnekt.shared.domain.chats

import com.connectycube.chat.ConnectycubeChatService
import com.connectycube.chat.model.ConnectycubeChatDialog
import com.connectycube.chat.model.ConnectycubeDialogType
import knnekt.shared.data.entity.Chat
import knnekt.shared.domain.UseCase
import knnekt.shared.result.Result
import knnekt.shared.result.catch

class GetChatConnectionUseCase : UseCase<Chat, ChatConnection>() {

    override fun execute(parameters: Chat): ChatConnection {
        return object : ChatConnection {

            val chat = ConnectycubeChatDialog(parameters.id).apply {
                setOccupantsIds(parameters.occupants)
                type = ConnectycubeDialogType.parseByCode(parameters.type)
                initForChat(ConnectycubeChatService.getInstance())
            }

            override suspend fun sendMessage(text: String): Result<Unit> {
                return Result.catch {
                    chat.sendMessage(text)
                }
            }

        }
    }
}