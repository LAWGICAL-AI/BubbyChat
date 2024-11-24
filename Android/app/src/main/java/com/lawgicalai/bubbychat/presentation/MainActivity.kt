package com.lawgicalai.bubbychat.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lawgicalai.bubbychat.domain.usecase.SignOutUseCase
import com.lawgicalai.bubbychat.presentation.mypage.MyPageViewModel
import com.lawgicalai.bubbychat.presentation.navigation.MainNavHost
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var signOutUseCase: SignOutUseCase
    private val viewModel: MyPageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initializeUseCases(
            signOutUseCase,
        )
        setContent {
            BubbyChatTheme {
                MainNavHost(myPageViewModel = viewModel)
            }
        }
    }
}
