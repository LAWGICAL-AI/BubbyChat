package com.lawgicalai.bubbychat.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lawgicalai.bubbychat.data.room.dao.ChatMessageDao
import com.lawgicalai.bubbychat.data.room.entity.ChatMessageEntity
import com.lawgicalai.bubbychat.data.room.entity.ChatSessionEntity
import com.lawgicalai.bubbychat.data.room.utils.Converters

@Database(
    entities = [ChatMessageEntity::class, ChatSessionEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ChatDatabase : RoomDatabase() {
    abstract fun chatMessageDao(): ChatMessageDao
}
