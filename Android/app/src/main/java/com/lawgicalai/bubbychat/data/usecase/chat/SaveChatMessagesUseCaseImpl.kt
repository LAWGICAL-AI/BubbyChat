package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.room.dao.ChatMessageDao
import com.lawgicalai.bubbychat.data.room.entity.ChatMessageEntity
import com.lawgicalai.bubbychat.data.room.entity.ChatSessionEntity
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.usecase.SaveChatMessagesUseCase
import java.time.LocalDateTime
import javax.inject.Inject

class SaveChatMessagesUseCaseImpl
    @Inject
    constructor(
        private val chatMessageDao: ChatMessageDao,
    ) : SaveChatMessagesUseCase {
        override suspend fun invoke(
            messages: List<ChatMessage>,
            email: String,
        ) {
            // 새로운 세션을 생성
            if (!messages[1].text.contains("오류가 발생했습니다")) {
                val session =
                    ChatSessionEntity(
                        title = messages.first().text,
                        firstResponse = messages[1].text,
                        email = email,
                    )
                val sessionId = chatMessageDao.insertSession(session).toInt()

                // 메시지에 sessionId 할당하여 저장
                val messageEntities =
                    messages.map { chatMessage ->
                        ChatMessageEntity(
                            text = chatMessage.text,
                            isMine = chatMessage.isMine,
                            timestamp = LocalDateTime.now(),
                            sessionId = sessionId,
                            email = email,
                        )
                    }
                chatMessageDao.insertMessages(messageEntities)
            }
        }
    }
