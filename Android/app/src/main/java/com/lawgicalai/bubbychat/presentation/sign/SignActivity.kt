package com.lawgicalai.bubbychat.presentation.sign

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.lawgicalai.bubbychat.domain.usecase.SignInWithGoogleUseCase
import com.lawgicalai.bubbychat.presentation.MainActivity
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignActivity : ComponentActivity() {
    // ActivityContext라서 주입해서 써야겠다고 판단
    @Inject
    lateinit var signInWithGoogleUseCase: SignInWithGoogleUseCase

    private val viewModel: SignViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initializeUseCases(
            signInWithGoogleUseCase,
        )
        setContent {
            BubbyChatTheme {
                SignScreen(onLoginSuccess = {
                    startActivity(
                        Intent(this, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        },
                    )
                    finish()
                })
            }
        }
    }
}
