package com.lawgicalai.bubbychat.presentation.home

import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.presentation.chat.ChatSideEffect
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
){
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Toast -> {
                Toast.makeText(context, it.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
private fun HomeScreen(){

}

@Preview
@Composable
fun HomeScreenPreview(){
    BubbyChatTheme {
        HomeScreen()
    }
}

