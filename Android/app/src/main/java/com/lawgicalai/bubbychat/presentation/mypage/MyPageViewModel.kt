package com.lawgicalai.bubbychat.presentation.mypage

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.ClearTokenUseCase
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import com.lawgicalai.bubbychat.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject


private const val TAG = "MyPageViewModel"

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
        private val clearTokenUseCase: ClearTokenUseCase,
    ) : ViewModel(),
        ContainerHost<MyPageState, MyPageSideEffect> {
        override val container: Container<MyPageState, MyPageSideEffect> =
            container(
                initialState = MyPageState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { coroutineContext, throwable ->
                            intent {
                                postSideEffect(MyPageSideEffect.Toast("예외발생 ${throwable.message}"))
                            }
                        }
                },
            )

        init {
            getCurrentUser()
        }

        private var signOutUseCase: SignOutUseCase? = null

        fun initializeUseCases(signOutUseCase: SignOutUseCase) {
            this.signOutUseCase = signOutUseCase
        }

        private fun getCurrentUser() {
            intent {
                getCurrentUserUseCase().collect { user ->
                    user?.let {
                        reduce { state.copy(userInfo = user) }
                    }
                }
            }
        }

        fun signOut() {
            intent {
                signOutUseCase
                    ?.invoke()
                    ?.onSuccess {
                        Timber.tag(TAG).d("signOut: $it")
                        reduce {
                            state.copy(
                                userInfo =
                                    User(
                                        email = null,
                                        displayName = null,
                                        profileImage = null,
                                    ),
                            )
                        }
                        clearTokenUseCase()
                        postSideEffect(MyPageSideEffect.Restart)
                        postSideEffect(MyPageSideEffect.Toast("로그아웃 되었습니다."))
                    }?.onFailure {
                        postSideEffect(MyPageSideEffect.Toast("예외 발생: ${it.message}"))
                    }
            }
        }
    }

@Immutable
data class MyPageState(
    val userInfo: User = User(email = null, displayName = null, profileImage = null),
)

sealed interface MyPageSideEffect {
    data class Toast(
        val message: String,
    ) : MyPageSideEffect

    data object Restart : MyPageSideEffect
}
