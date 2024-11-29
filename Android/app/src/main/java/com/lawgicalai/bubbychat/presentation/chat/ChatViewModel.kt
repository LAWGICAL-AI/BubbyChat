package com.lawgicalai.bubbychat.presentation.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.model.PrecedentBody
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetPrecedentResponseUseCase
import com.lawgicalai.bubbychat.domain.usecase.SaveChatMessagesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.network.sockets.SocketTimeoutException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

private const val TAG = "MainViewModel"

@HiltViewModel
class ChatViewModel
    @Inject
    constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getChatResponseStreamUseCase: GetChatResponseStreamUseCase,
        private val getPrecedentResponseUseCase: GetPrecedentResponseUseCase,
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

        init {
            getCurrentUser()
        }

        private var dotsJob: Job? = null

        private fun cancelDots() {
            dotsJob?.let { job ->
                if (job.isActive) {
                    job.cancel()
                }
            }
            dotsJob = null
        }

        fun getResponse(question: String) =
            intent {
                getChatResponse(question)
                if (state.personaType == "expert") {
                    getPrecedentResponse(question)
                }
            }

        private fun getPrecedentResponse(question: String) =
            intent {
                getPrecedentResponseUseCase(question)
                    .onEach { data ->
                        Timber.tag(TAG).d("$data")
                        reduce {
                            state.copy(selectedPrecedent = data.getOrNull()?.precedent ?: emptyList())
                        }
                    }.launchIn(viewModelScope)
            }

        private fun getChatResponse(question: String) =
            intent {
                cancelDots()
                reduce { state.copy(messages = state.messages + ChatMessage(question, isMine = true)) }
                reduce { state.copy(input = "") }

                val initialResponseIndex = state.messages.size
                reduce { state.copy(messages = state.messages + ChatMessage("...", isMine = false)) }

                dotsJob =
                    viewModelScope.launch {
                        try {
                            while (isActive) {
                                delay(500)
                                reduce {
                                    val updatedMessages = state.messages.toMutableList()
                                    if (initialResponseIndex < updatedMessages.size) {
                                        val dotsMessage = updatedMessages[initialResponseIndex].text
                                        val newDotsMessage =
                                            if (dotsMessage.length >= 6) "." else "$dotsMessage."
                                        updatedMessages[initialResponseIndex] =
                                            ChatMessage(newDotsMessage, isMine = false)
                                        state.copy(messages = updatedMessages)
                                    } else {
                                        state
                                    }
                                }
                            }
                        } catch (e: CancellationException) {
                            // 코루틴 취소는 정상적인 흐름이므로 별도 처리 없이 종료
                        } finally {
                            // cleanup 코드가 필요한 경우 여기에 작성
                        }
                    }

                getChatResponseStreamUseCase(question)
                    .onEach { response ->
                        response
                            .onSuccess { data ->
                                cancelDots()
                                reduce {
                                    val updatedMessages = state.messages.toMutableList()
                                    // 인덱스가 유효한지 안전하게 확인합니다
                                    if (initialResponseIndex < updatedMessages.size) {
                                        val currentMessage = updatedMessages[initialResponseIndex]
                                        val newContent =
                                            if (currentMessage.text.startsWith(".")) {
                                                // 첫 응답이면 로딩 점들을 대체합니다
                                                data
                                            } else {
                                                // 기존 응답에 추가합니다
                                                currentMessage.text + data
                                            }
                                        updatedMessages[initialResponseIndex] =
                                            ChatMessage(newContent, isMine = false)
                                        state.copy(messages = updatedMessages)
                                    } else {
                                        // 인덱스가 유효하지 않다면 새 메시지로 추가합니다
                                        state.copy(
                                            messages =
                                                state.messages +
                                                    ChatMessage(
                                                        data,
                                                        isMine = false,
                                                    ),
                                        )
                                    }
                                }
                            }.onFailure {
                                cancelDots()
                                val errorMessage =
                                    if (it is SocketTimeoutException) {
                                        "응답 시간이 초과되었습니다"
                                    } else {
                                        "오류가 발생했습니다. 다시 시도해주세요"
                                    }

                                reduce {
                                    val updatedMessages = state.messages.toMutableList()
                                    if (initialResponseIndex < updatedMessages.size) {
                                        updatedMessages[initialResponseIndex] =
                                            ChatMessage(errorMessage, isMine = false)
                                        state.copy(messages = updatedMessages)
                                    } else {
                                        state.copy(
                                            messages =
                                                state.messages +
                                                    ChatMessage(
                                                        errorMessage,
                                                        isMine = false,
                                                    ),
                                        )
                                    }
                                }
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
                if (state.messages.size > 1 && state.messages[1].text.length > 6) {
                    saveChatMessagesUseCase(
                        state.messages,
                        state.userInfo.email.orEmpty(),
                    )
                }
                reduce {
                    state.copy(
                        messages = emptyList(),
                    )
                }
            }

        fun resetResponse() =
            intent {
                reduce {
                    state.copy(
                        isResponseComplete = false,
                    )
                }
            }

        fun selectPersona(personaType: String) =
            intent {
                reduce {
                    Timber.tag(TAG).d("selected: $personaType")
                    state.copy(personaType = personaType)
                }
            }

        private fun getCurrentUser() =
            intent {
                getCurrentUserUseCase().collect { user ->
                    user?.let {
                        reduce { state.copy(userInfo = user) }
                    }
                }
            }

        fun showPrecedentDetail() =
            intent {
                reduce {
                    state.copy(isShowDialog = true)
                }
            }

        fun hidePrecedentDetail() =
            intent {
                reduce {
                    state.copy(isShowDialog = false)
                }
            }

        fun clean() =
            intent {
                cancelDots()
                saveMessages()
            }

        override fun onCleared() {
            super.onCleared()
            clean()
        }
    }

@Immutable
data class ChatState(
    val userInfo: User = User(email = null, displayName = null, profileImage = null),
    val input: String = "",
    val messages: List<ChatMessage> = emptyList(),
    val isResponseComplete: Boolean = false,
    val personaType: String = "",
    val isShowDialog: Boolean = false,
    val selectedPrecedent: List<PrecedentBody.Precedent> = emptyList(),
)

sealed interface ChatSideEffect {
    data class Toast(
        val massage: String,
    ) : ChatSideEffect
}
