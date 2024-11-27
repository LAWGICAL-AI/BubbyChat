package com.lawgicalai.bubbychat.presentation.route

import com.lawgicalai.bubbychat.R

sealed interface Route {
    val route: String
    val contentDescription: String
    val iconResId: Int
}

enum class MainRoute(
    override val route: String,
    override val contentDescription: String,
    override val iconResId: Int,
) : Route {
    HOME(route = "HomeScreen", contentDescription = "홈", iconResId = R.drawable.ic_home),
    MYPAGE(route = "MyPageScreen", contentDescription = "내정보", iconResId = R.drawable.ic_user),
}

enum class ChatRoute(
    override val route: String,
    override val contentDescription: String,
    override val iconResId: Int,
) : Route {
    PERSONA(route = "PersonaScreen", contentDescription = "페르소나", iconResId = R.drawable.ic_send),
    CHAT(route = "ChatScreen", contentDescription = "채팅", iconResId = R.drawable.ic_send),
}
