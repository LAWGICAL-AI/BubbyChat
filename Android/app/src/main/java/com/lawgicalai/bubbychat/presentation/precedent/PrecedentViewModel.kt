package com.lawgicalai.bubbychat.presentation.precedent

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import com.lawgicalai.bubbychat.domain.model.Precedent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class PrecedentViewModel
    @Inject
    constructor() :
    ViewModel(),
        ContainerHost<PrecedentState, PrecedentSideEffect> {
        override val container: Container<PrecedentState, PrecedentSideEffect> =
            container(
                initialState = PrecedentState(),
                buildSettings = {
                    this.exceptionHandler =
                        CoroutineExceptionHandler { coroutineContext, throwable ->
                            intent {
                                postSideEffect(PrecedentSideEffect.Toast("예외발생 ${throwable.message}"))
                            }
                        }
                },
            )
    }

@Immutable
data class PrecedentState(
    val precedents: List<Precedent> = emptyList(),
    val selectedPrecedent: Precedent? = null,
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
)

sealed interface PrecedentSideEffect {
    data class Toast(
        val message: String,
    ) : PrecedentSideEffect
}
