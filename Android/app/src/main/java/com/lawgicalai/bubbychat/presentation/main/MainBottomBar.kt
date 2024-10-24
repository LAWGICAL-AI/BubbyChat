package com.lawgicalai.bubbychat.presentation.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.lawgicalai.bubbychat.presentation.route.MainRoute
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.Orange

@Composable
fun MainBottomBar(
    navController: NavController,
) {
    val context = LocalContext.current
    val navBackStackEntry by navController.currentBackStackEntryAsState() // 백스택 긁어오기
    val currentRoute: MainRoute = navBackStackEntry?.destination?.route?.let { currentRoute ->
        MainRoute.entries.find { route -> route.route == currentRoute }
    } ?: MainRoute.HOME // 현재 라우트 가져오기
    // ?:로 처리해서 non-null로 처리
    MainBottomBar(
        currentRoute = currentRoute,
        onItemClick = { newRoute ->
            navController.navigate(route = newRoute.route) {
                navController.graph.startDestinationRoute?.let {
                    // 시작 위치 가져오기
                    popUpTo(it) { // 시작 위치로 이동
                        // 상태를 저장하겠냐
                        saveState = true
                    }
                    launchSingleTop = true // 하나의 그래프에서 하나만 띄울건지
                    restoreState = true
                }
            }
        }
    )
}

@Composable
fun MainBottomBar(
    currentRoute: MainRoute,
    onItemClick: (MainRoute) -> Unit,
) {
    // 네비게이션 컨트롤러가 들어가있어야됨
    Column() {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 16.dp)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            MainRoute.entries.forEach { route ->
                Column(modifier = Modifier.clickable {
                    onItemClick(route) // 위로 올려줌,
                }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = ImageVector.vectorResource(route.iconResId),
                        contentDescription = route.contentDescription,
                        tint = if (currentRoute == route) {
                            Orange
                        } else {
                            Color.Gray
                        }
                    )
                    Text(
                        style = MaterialTheme.typography.labelMedium,
                        text = route.contentDescription, color = if (currentRoute == route) {
                            Orange
                        } else {
                            Color.Gray
                        }
                    )
                }
            }
        }
    }

}

@Preview
@Composable
private fun MainBottomBarPreview() {
    //MainBottomBar()
    var currentRoute by remember {
        mutableStateOf(MainRoute.HOME)
    }
    BubbyChatTheme {
        MainBottomBar(currentRoute = currentRoute, onItemClick = { route ->
            currentRoute = route
        })
    }
}