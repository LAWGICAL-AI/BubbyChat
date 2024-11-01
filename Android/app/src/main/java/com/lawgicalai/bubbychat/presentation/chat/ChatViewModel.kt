package com.lawgicalai.bubbychat.presentation.chat

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getChatResponseStreamUseCase: GetChatResponseStreamUseCase
) : ViewModel(), ContainerHost<ChatState, ChatSideEffect> {
    override val container: Container<ChatState, ChatSideEffect> = container(
        initialState = ChatState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
                intent {
                    postSideEffect(ChatSideEffect.Toast("예외발생 ${throwable.message}"))
                }
            }
        }
    )

    fun getResponse(question: String) = intent {
        getChatResponseStreamUseCase(question).onEach { response ->
            Timber.tag(TAG).d("getResponse: $response")
            response.onSuccess {
                reduce { state.copy(response = state.response + " " + response) }
            }.onFailure {
                postSideEffect(ChatSideEffect.Toast("예외발생 ${it.message}"))
            }
        }.launchIn(viewModelScope)
    }
}

@Immutable
data class ChatState(
    val response: String = "",
)

sealed interface ChatSideEffect {
    data class Toast(val massage: String) : ChatSideEffect
}