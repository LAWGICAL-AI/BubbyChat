package com.lawgicalai.bubbychat.presentation.home

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.GetAllChatSessionsUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetChatSessionUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "HomeViewModel"

@HiltViewModel
class ManualHomeViewModel
@Inject
constructor(
    private val getAllChatSessionsUseCase: GetAllChatSessionsUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getChatSessionUseCase: GetChatSessionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ManualHomeState.Empty)
    val uiState: StateFlow<ManualHomeState> = _uiState.asStateFlow()

    // Toast 메시지 같은 일회성 이벤트를 위한 SharedFlow
    private val _sideEffect = Channel<ManualHomeSideEffect>(capacity = Channel.BUFFERED)
    val sideEffect: Flow<ManualHomeSideEffect> = _sideEffect.receiveAsFlow()

    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            Timber.e(throwable, "Coroutine Exception")
            viewModelScope.launch {
                _sideEffect.send(ManualHomeSideEffect.Toast("예외발생: ${throwable.message}"))
                Timber.tag(TAG).e(throwable, "예외발생: ${throwable.message}")
            }
        }

    init {
        processIntent(HomeIntent.LoadInitialData)
    }

    fun processIntent(intent: HomeIntent){
        when(intent){
            is HomeIntent.LoadInitialData -> {
                getCurrentUser()
            }
            is HomeIntent.GetChatSession -> getChatSession(intent.sessionId)
            is HomeIntent.GetAllSessions -> getAllSessions()
            is HomeIntent.ShowSessionDetail -> showSessionDetail()
            is HomeIntent.HideSessionDetail -> hideSessionDetail()
        }
    }

    private fun getChatSession(sessionId: Int) =
        viewModelScope.launch(exceptionHandler) {
            _uiState.value.userInfo.email?.let { email ->
                val chatMessages = getChatSessionUseCase(email = email, sessionId = sessionId)
                Timber.tag(TAG).d("$chatMessages")
                _uiState.update {
                    it.copy(selectedSession = chatMessages)
                }
            }
        }

    private fun getAllSessions() {
        viewModelScope.launch(exceptionHandler) {
            _uiState.value.userInfo.email?.let { email ->
                val sessions = getAllChatSessionsUseCase(email).firstOrNull() ?: emptyList()
                Timber.tag(TAG).d("getAllSessions: $sessions")
                _uiState.update {
                    it.copy(sessions = sessions)
                }
            }
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch(exceptionHandler) {
            getCurrentUserUseCase().firstOrNull()?.let { user ->
                _uiState.update { it.copy(userInfo = user) }
                // 유저 정보를 가져온 후 세션 목록 로드
                getAllSessions()
            }
        }
    }

    private fun showSessionDetail() {
        _uiState.update {
            it.copy(isShowDialog = true)
        }
    }

    private fun hideSessionDetail() {
        _uiState.update {
            it.copy(isShowDialog = false)
        }
    }
}

@Immutable
data class ManualHomeState(
    val sessions: List<ChatSession>,
    val userInfo: User,
    val selectedSession: List<ChatMessage>,
    val isShowDialog: Boolean,
){
    companion object{
        val Empty = ManualHomeState(
            sessions = emptyList(),
            userInfo = User(email = null, displayName = null, profileImage = null),
            selectedSession = emptyList(),
            isShowDialog = false,
        )
    }
}

sealed interface HomeIntent {
    data class GetChatSession(val sessionId: Int) : HomeIntent // 설계도일뿐, input에 따라 객체를 생성해서 쓴다.
    data object GetAllSessions : HomeIntent // 데이터가 없으면 object로 정의 -> 매번 생성하지 않는다.
    data object ShowSessionDetail : HomeIntent
    data object HideSessionDetail : HomeIntent
    // 초기 데이터 로딩을 위한 Intent 추가
    data object LoadInitialData : HomeIntent
}

sealed interface ManualHomeSideEffect {
    data class Toast(val message: String) : ManualHomeSideEffect
}
