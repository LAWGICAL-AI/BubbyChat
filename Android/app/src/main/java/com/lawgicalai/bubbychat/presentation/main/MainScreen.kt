package com.lawgicalai.bubbychat.presentation.main

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lawgicalai.bubbychat.ui.theme.BubbyChatTheme

@Composable
fun MainScreen(){
    Greeting("This is MainScreen")
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = name,
        modifier = modifier
    )
}

@Preview
@Composable
fun MainScreenPreview(){
    BubbyChatTheme {
        MainScreen()
    }
}

