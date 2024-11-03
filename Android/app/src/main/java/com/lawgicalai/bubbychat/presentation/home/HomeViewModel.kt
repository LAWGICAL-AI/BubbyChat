package com.lawgicalai.bubbychat.presentation.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
            getAllSessions()
        }

        fun getAllSessions() =
            blockingIntent {
                val sessions = getAllChatSessionsUseCase()
                Timber.tag(TAG).d("getAllSessions: $sessions")
                reduce {
                    state.copy(
                        sessions = sessions,
                    )
                }
            }
    }

@Immutable
data class HomeState(
    val sessions: List<ChatSession> = emptyList(),
)

sealed interface HomeSideEffect {
    data class Toast(
        val massage: String,
    ) : HomeSideEffect
}
