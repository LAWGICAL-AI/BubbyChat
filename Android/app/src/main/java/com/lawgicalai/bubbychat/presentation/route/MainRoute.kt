package com.lawgicalai.bubbychat.presentation.route

import com.lawgicalai.bubbychat.R


enum class MainRoute(
    val route: String,
    val contentDescription: String,
    val iconResId: Int
) {
    HOME(route = "HomeScreen", contentDescription = "홈", iconResId = R.drawable.ic_home),
    CHAT(route = "ChatScreen", contentDescription = "채팅", iconResId = R.drawable.ic_question_answer),
    SETTING(route = "SettingScreen", contentDescription = "내정보", iconResId = R.drawable.ic_face)
}