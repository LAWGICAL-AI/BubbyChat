package com.lawgicalai.bubbychat.presentation.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetChatSessionUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val getAllChatSessionsUseCase: GetAllChatSessionsUseCase,
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val getChatSessionUseCase: GetChatSessionUseCase,
    ) : ViewModel(),
        ContainerHost<HomeState, HomeSideEffect> {
        override val container: Container<HomeState, HomeSideEffect> =
            container(
                initialState = HomeState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { coroutineContext, throwable ->
                            intent {
                                postSideEffect(HomeSideEffect.Toast("예외발생 ${throwable.message}"))
                            }
                        }
                },
            )

        init {
            getCurrentUser()
        }

        fun getChatSession(sessionId: Int) =
            intent {
                state.userInfo.email?.let {
                    val chatMessages = getChatSessionUseCase(email = it, sessionId = sessionId)
                    Timber.tag(TAG).d("$chatMessages")
                    reduce {
                        state.copy(
                            selectedSession = chatMessages,
                        )
                    }
                }
            }

        fun getAllSessions() =
            blockingIntent {
                state.userInfo.email?.let { email ->
                    val sessions = getAllChatSessionsUseCase(email).firstOrNull() ?: emptyList()
                    Timber.tag(TAG).d("getAllSessions: $sessions")
                    reduce {
                        state.copy(
                            sessions = sessions,
                        )
                    }
                }
            }

        private fun getCurrentUser() {
            intent {
                getCurrentUserUseCase().firstOrNull()?.let {
                    reduce { state.copy(userInfo = it) }
                    getAllSessions()
                }
            }
        }

        fun showSessionDetail() =
            intent {
                reduce {
                    state.copy(isShowDialog = true)
                }
            }

        fun hideSessionDetail() =
            intent {
                reduce {
                    state.copy(isShowDialog = false)
                }
            }
    }

@Immutable
data class HomeState(
    val sessions: List<ChatSession> = emptyList(),
    val userInfo: User = User(email = null, displayName = null, profileImage = null),
    val selectedSession: List<ChatMessage> = emptyList(),
    val isShowDialog: Boolean = false,
)

sealed interface HomeSideEffect {
    data class Toast(
        val massage: String,
    ) : HomeSideEffect
}
