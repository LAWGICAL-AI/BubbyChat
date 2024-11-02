package com.lawgicalai.bubbychat.presentation.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import com.lawgicalai.bubbychat.domain.usecase.SaveChatMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val getChatResponseStreamUseCase: GetChatResponseStreamUseCase,
        private val saveChatMessagesUseCase: SaveChatMessagesUseCase,
    ) : ViewModel(),
        ContainerHost<ChatState, ChatSideEffect> {
        override val container: Container<ChatState, ChatSideEffect> =
            container(
                initialState = ChatState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { coroutineContext, throwable ->
                            intent {
                                postSideEffect(ChatSideEffect.Toast("예외발생 ${throwable.message}"))
                            }
                        }
                },
            )

        fun getResponse(question: String) =
            intent {
                reduce { state.copy(messages = state.messages + ChatMessage(question, isMine = true)) }
                reduce { state.copy(input = "") }

                // 상대방 응답 대기 중임을 표시하기 위해 '...' 메시지를 추가
                val initialResponseIndex = state.messages.size
                reduce { state.copy(messages = state.messages + ChatMessage("...", isMine = false)) }

                // '...'을 능동적으로 변하게 하는 Job 시작
                val dotsJob =
                    viewModelScope.launch {
                        while (true) {
                            delay(500) // 0.5초마다 갱신
                            val updatedMessages = state.messages.toMutableList()
                            val dotsMessage = updatedMessages[initialResponseIndex].text

                            // '...', '......'을 번갈아가며 보여줌
                            val newDotsMessage = if (dotsMessage.length >= 6) "." else dotsMessage + "."
                            updatedMessages[initialResponseIndex] =
                                ChatMessage(newDotsMessage, isMine = false)
                            reduce { state.copy(messages = updatedMessages) }
                        }
                    }

                getChatResponseStreamUseCase(question)
                    .onEach { response ->
                        response
                            .onSuccess { data ->
                                dotsJob.cancel()
                                val updatedMessages = state.messages.toMutableList()
                                // 첫 번째 응답이 도착하면 '...' 메시지를 대체하여 응답 표시
                                if (updatedMessages[initialResponseIndex].text.startsWith(".")) {
                                    updatedMessages[initialResponseIndex] =
                                        ChatMessage("$data ", isMine = false)
                                } else {
                                    // 이후 데이터는 기존 메시지에 덧붙이기
                                    val currentResponse =
                                        updatedMessages[initialResponseIndex].text + data + " "
                                    updatedMessages[initialResponseIndex] =
                                        ChatMessage(currentResponse, isMine = false)
                                }

                                // 상태를 업데이트하여 실시간으로 UI에 반영
                                reduce { state.copy(messages = updatedMessages) }
                            }.onFailure {
                                dotsJob.cancel()
                                postSideEffect(ChatSideEffect.Toast("예외 발생: ${it.message}"))
                            }
                    }.launchIn(viewModelScope)
            }

        fun textInputChange(text: String) =
            blockingIntent {
                reduce { state.copy(input = text) }
            }

        fun saveMessages() =
            intent {
                if (state.messages.size > 1) saveChatMessagesUseCase(state.messages)
                reduce {
                    state.copy(
                        messages = emptyList(),
                    )
                }
            }
    }

@Immutable
data class ChatState(
    val input: String = "",
    val messages: List<ChatMessage> = emptyList(),
)

sealed interface ChatSideEffect {
    data class Toast(
        val massage: String,
    ) : ChatSideEffect
}
