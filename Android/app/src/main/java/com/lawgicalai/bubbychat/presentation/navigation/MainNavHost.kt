package com.lawgicalai.bubbychat.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.lawgicalai.bubbychat.presentation.chat.ChatScreen
import com.lawgicalai.bubbychat.presentation.chat.ChatViewModel
import com.lawgicalai.bubbychat.presentation.chat.PersonaScreen
import com.lawgicalai.bubbychat.presentation.home.HomeScreen
import com.lawgicalai.bubbychat.presentation.mypage.MyPageScreen
import com.lawgicalai.bubbychat.presentation.mypage.MyPageViewModel
import com.lawgicalai.bubbychat.presentation.route.ChatRoute
import com.lawgicalai.bubbychat.presentation.route.MainRoute
import com.lawgicalai.bubbychat.presentation.route.Route

@Composable
fun MainNavHost(myPageViewModel: MyPageViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute =
        navBackStackEntry?.destination?.route?.let { route ->
            MainRoute.entries.find { it.route == route }
                ?: ChatRoute.entries.find { it.route == route }
        } ?: MainRoute.HOME

    var previousRoute by remember { mutableStateOf<Route?>(null) }
    val chatViewModel: ChatViewModel = hiltViewModel()
    LaunchedEffect(currentRoute) {
        if (previousRoute == ChatRoute.CHAT) {
            navController.currentBackStackEntry?.savedStateHandle?.set("refresh", true)
        }
        previousRoute = currentRoute
    }

    Scaffold(
        modifier = Modifier.background(Color.White),
        content = { padding ->
            NavHost(
                modifier = Modifier.padding(padding),
                navController = navController,
                startDestination = "main",
                exitTransition = { ExitTransition.None },
                enterTransition = { EnterTransition.None },
            ) {
                // 메인 그래프
                navigation(
                    startDestination = MainRoute.HOME.route,
                    route = "main",
                ) {
                    composable(route = MainRoute.HOME.route) {
                        HomeScreen(onStartClick = {
                            navController.navigate("chat") {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                    }
                    composable(route = MainRoute.MYPAGE.route) {
                        MyPageScreen(myPageViewModel = myPageViewModel)
                    }

                    navigation(
                        startDestination = ChatRoute.PERSONA.route,
                        route = "chat",
                    ) {
                        composable(route = ChatRoute.CHAT.route) {
                            ChatScreen(viewModel = chatViewModel)
                        }
                        composable(route = ChatRoute.PERSONA.route) {
                            PersonaScreen(
                                viewModel = chatViewModel,
                                onPersonaSelected = {
                                    navController.navigate(ChatRoute.CHAT.route) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            )
                        }
                    }
                }
            }
        },
        bottomBar = {
            BottomBarWithFAB(
                navController = navController,
                currentRoute = currentRoute,
                onFabClick = {
                    navController.navigate("chat") {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        },
    )
}
