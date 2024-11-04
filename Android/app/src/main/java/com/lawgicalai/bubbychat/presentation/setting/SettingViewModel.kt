package com.lawgicalai.bubbychat.presentation.setting

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.presentation.chat.ChatSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(): ViewModel(), ContainerHost<SettingState, SettingSideEffect> {
    override val container: Container<SettingState, SettingSideEffect> = container(
        initialState = SettingState(),
        buildSettings = {
            this.exceptionHandler =
                CoroutineExceptionHandler { coroutineContext, throwable ->
                    intent {
                        postSideEffect(SettingSideEffect.Toast("예외발생 ${throwable.message}"))
                    }
                }
        },
    )

}

@Immutable
data class SettingState(
    val name: String = ""
)

sealed interface SettingSideEffect{
    data class Toast(val message: String): SettingSideEffect
}