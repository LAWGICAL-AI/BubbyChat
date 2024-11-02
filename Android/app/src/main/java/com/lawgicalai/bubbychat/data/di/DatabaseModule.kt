package com.lawgicalai.bubbychat.data.di

import android.content.Context
import androidx.room.Room
import com.lawgicalai.bubbychat.data.room.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context,
    ): ChatDatabase =
        Room
            .databaseBuilder(
                context.applicationContext,
                ChatDatabase::class.java,
                "chat_database",
            ).build()

    @Provides
    @Singleton
    fun provideChatMessageDao(database: ChatDatabase) = database.chatMessageDao()
}
