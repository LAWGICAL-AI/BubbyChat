package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.room.dao.ChatMessageDao
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.usecase.GetChatSessionUseCase
import javax.inject.Inject

class GetChatSessionUseCaseImpl
    @Inject
    constructor(
        private val chatMessageDao: ChatMessageDao,
    ) : GetChatSessionUseCase {
        override suspend fun invoke(
            sessionId: Int,
            email: String,
        ): List<ChatMessage> =
            chatMessageDao.getMessagesForSession(sessionId = sessionId, email = email).map {
                ChatMessage(text = it.text, isMine = it.isMine)
            }
    }
