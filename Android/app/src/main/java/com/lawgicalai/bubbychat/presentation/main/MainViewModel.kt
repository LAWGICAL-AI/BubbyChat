package com.lawgicalai.bubbychat.presentation.main

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lawgicalai.bubbychat.domain.usecase.GetChatResponseStreamUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

private const val TAG = "MainViewModel"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getChatResponseStreamUseCase: GetChatResponseStreamUseCase
) : ViewModel(), ContainerHost<MainState, MainSideEffect> {
    override val container: Container<MainState, MainSideEffect> = container(
        initialState = MainState(),
        buildSettings = {
            this.exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
                intent {
                    postSideEffect(MainSideEffect.Toast("예외발생 ${throwable.message}"))
                }
            }
        }
    )

    fun getResponse(question: String) = intent {
        getChatResponseStreamUseCase(question).onSuccess { flow ->
            flow.onEach { response ->
                Log.d(TAG, "getResponse: $response")
                reduce { state.copy(response = state.response +" "+ response) }
            }.launchIn(viewModelScope)
        }.onFailure { e ->
            Log.d(TAG, "getResponse: $e")
        }
    }
}

@Immutable
data class MainState(
    val response: String = "",
)

sealed interface MainSideEffect {
    data class Toast(val massage: String) : MainSideEffect
}