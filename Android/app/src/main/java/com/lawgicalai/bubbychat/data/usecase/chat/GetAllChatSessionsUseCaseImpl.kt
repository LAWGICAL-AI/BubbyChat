package com.lawgicalai.bubbychat.data.usecase.chat

import com.lawgicalai.bubbychat.data.room.dao.ChatMessageDao
import com.lawgicalai.bubbychat.data.room.entity.ChatSessionEntity
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import javax.inject.Inject

class GetAllChatSessionsUseCaseImpl
    @Inject
    constructor(
        private val chatMessageDao: ChatMessageDao,
    ) : GetAllChatSessionsUseCase {
        override suspend operator fun invoke(): List<ChatSessionEntity> = chatMessageDao.getAllSessions()
    }
