package com.lawgicalai.bubbychat.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.presentation.route.ChatRoute
import com.lawgicalai.bubbychat.presentation.route.MainRoute
import com.lawgicalai.bubbychat.presentation.route.Route
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkerGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import com.lawgicalai.bubbychat.presentation.utils.noRippleClickable

@Composable
fun BottomBarWithFAB(
    navController: NavController,
    currentRoute: Route,
    onFabClick: () -> Unit,
) {
    Box(modifier = Modifier.background(color = Color.Transparent).zIndex(1f)) {
        BottomBar(
            currentRoute = currentRoute,
            onItemClick = { newRoute ->
                navController.navigate(newRoute.route) {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            },
            modifier = Modifier.align(Alignment.BottomCenter),
        )

        Icon(
            painter = painterResource(R.drawable.ic_send),
            contentDescription = "chat",
            tint = if (currentRoute in ChatRoute.entries) Color.White else BubbyDarkerGreen,
            modifier =
                Modifier
                    .padding(bottom = 12.dp)
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(BubbyDarkGreen)
                    .align(Alignment.BottomCenter)
                    .noRippleClickable {
                        onFabClick()
                    }.padding(12.dp),
        )
    }
}

@Composable
fun BottomBar(
    currentRoute: Route,
    onItemClick: (Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(BubbyGreen)
                .padding(vertical = 18.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MainRoute.entries.forEach { route ->
            Icon(
                painter = painterResource(route.iconResId),
                contentDescription = route.contentDescription,
                tint = if (currentRoute == route) Color.White else BubbyDarkerGreen,
                modifier =
                    Modifier
                        .size(28.dp)
                        .noRippleClickable { onItemClick(route) },
            )
        }
    }
}
