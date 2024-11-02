package com.lawgicalai.bubbychat.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lawgicalai.bubbychat.presentation.chat.ChatScreen
import com.lawgicalai.bubbychat.presentation.route.MainRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.background(Color.White),
        content = { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = MainRoute.HOME.route,
            ) {
                // navHost에 의해 composable screen이 관리된다.
                composable(route = MainRoute.HOME.route) {
//                        BoardScreen()
                }
                composable(route = MainRoute.SETTING.route) {
//                        SettingScreen()
                }
                composable(route = MainRoute.CHAT.route) {
                    ChatScreen()
                }
            }
        },
        bottomBar = {
            MainBottomBar(
                navController = navController,
            )
        },
    )
}
