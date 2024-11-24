package com.lawgicalai.bubbychat.presentation.sign

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.domain.usecase.GetCurrentUserUseCase
import com.lawgicalai.bubbychat.domain.usecase.SignInWithGoogleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.firstOrNull
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "SignViewModel"

@HiltViewModel
class SignViewModel
    @Inject
    constructor(
        private val getCurrentUserUseCase: GetCurrentUserUseCase,
    ) : ViewModel(),
        ContainerHost<SignState, SignSideEffect> {
        override val container: Container<SignState, SignSideEffect> =
            container(
                initialState = SignState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { coroutineContext, throwable ->
                            intent {
                                postSideEffect(SignSideEffect.Toast("예외발생 ${throwable.message}"))
                            }
                        }
                },
            )

        private var signInWithGoogleUseCase: SignInWithGoogleUseCase? = null

        fun initializeUseCases(signInWithGoogleUseCase: SignInWithGoogleUseCase) {
            this.signInWithGoogleUseCase = signInWithGoogleUseCase
        }

        fun googleSignIn() {
            intent {
                signInWithGoogleUseCase
                    ?.invoke()
                    ?.onSuccess { it ->
                        Timber.tag(TAG).d("googleSignIn: $it")
                        reduce { state.copy(userInfo = it) }
                    }?.onFailure {
                        postSideEffect(SignSideEffect.Toast("예외 발생: ${it.message}"))
                    }
            }
        }

        fun getCurrentUser() {
            intent {
                getCurrentUserUseCase().firstOrNull()?.let {
                    reduce { state.copy(userInfo = it) }
                }
            }
        }
    }

@Immutable
data class SignState(
    val userInfo: User = User(email = null, displayName = null, profileImage = null),
)

sealed interface SignSideEffect {
    data class Toast(
        val massage: String,
    ) : SignSideEffect
}
