package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.room.dao.ChatMessageDao
import com.lawgicalai.bubbychat.data.utils.formatToKoreanDate
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import javax.inject.Inject

class GetAllChatSessionsUseCaseImpl
    @Inject
    constructor(
        private val chatMessageDao: ChatMessageDao,
    ) : GetAllChatSessionsUseCase {
        override suspend operator fun invoke(): List<ChatSession> =
            chatMessageDao.getAllSessions().map { entity ->
                ChatSession(
                    text = entity.title,
                    timestamp = entity.startTime.toString().formatToKoreanDate(),
                    sessionId = entity.sessionId,
                    firstResponse = entity.firstResponse,
                )
            }
    }
