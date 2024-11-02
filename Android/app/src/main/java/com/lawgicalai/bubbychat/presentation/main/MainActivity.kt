package com.lawgicalai.bubbychat.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.lawgicalai.bubbychat.presentation.navigation.MainNavHost
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BubbyChatTheme {
                MainNavHost()
            }
        }
    }
}
