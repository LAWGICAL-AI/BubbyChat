package com.lawgicalai.bubbychat.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lawgicalai.bubbychat.presentation.chat.ChatScreen
import com.lawgicalai.bubbychat.presentation.home.HomeScreen
import com.lawgicalai.bubbychat.presentation.route.MainRoute
import com.lawgicalai.bubbychat.presentation.setting.SettingScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route?.let { route ->
            MainRoute.entries.find { it.route == route }
        } ?: MainRoute.HOME

    Scaffold(
        modifier = Modifier.background(Color.White),
        content = { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = MainRoute.HOME.route,
                exitTransition = { ExitTransition.None },
                enterTransition = { EnterTransition.None },
            ) {
                composable(route = MainRoute.HOME.route) {
                    HomeScreen(onStartClick = {
                        // 시작하기 버튼 클릭 시 FAB 클릭 이벤트 트리거
                        navController.navigate(MainRoute.CHAT.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }
                composable(route = MainRoute.SETTING.route) {
                    SettingScreen()
                }
                composable(route = MainRoute.CHAT.route) {
                    ChatScreen()
                }
            }
        },
        bottomBar = {
            BottomBarWithFAB(
                navController = navController,
                currentRoute = currentRoute,
                onFabClick = {
                    navController.navigate(MainRoute.CHAT.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    )
}
