package com.lawgicalai.bubbychat.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.presentation.route.MainRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable // NavHost도 Composable
fun MainNavHost() {
    val navController = rememberNavController()
    Surface {
        Scaffold(
            topBar = {
                TopAppBar(
                    modifier = Modifier.background(MaterialTheme.colorScheme.background),
                    title = {
                        Text(text = stringResource(R.string.app_name))
                    }
                )
            },
            content = { padding ->
                NavHost(
                    modifier = Modifier.padding(padding),
                    navController = navController,
                    startDestination = MainRoute.HOME.route
                ) {
                    // navHost에 의해 composable screen이 관리된다.
                    composable(route = MainRoute.HOME.route) {
//                        BoardScreen()
                    }
                    composable(route = MainRoute.SETTING.route) {
//                        SettingScreen()
                    }
                }
            },
            bottomBar = {
                MainBottomBar(
                    navController = navController
                )
            }
        )
    }
}