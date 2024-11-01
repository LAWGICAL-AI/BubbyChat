package com.lawgicalai.bubbychat.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect


@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    viewModel.collectSideEffect {
        when (it) {
            is ChatSideEffect.Toast -> {
                Toast.makeText(context, it.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    ChatScreen(onSendQuestion = viewModel::getResponse, state.response)
}

@Composable
private fun ChatScreen(
    onSendQuestion: (String) -> Unit,
    responseText: String
) {
    val scrollState = rememberScrollState()
    Surface {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .verticalScroll(scrollState)) {
            Button(
                onClick = {
                    onSendQuestion("How to put an elephant into refrigerator")
//                    onSendQuestion("코끼리 잡는법")
//                    onSendQuestion("how to get money")
                }
            ) {
                Text("Send")
            }
            Spacer(modifier = Modifier.height(40.dp))
            Text(modifier = Modifier.fillMaxWidth(), text = responseText)
        }
    }
}

@Preview
    @Composable
    fun ChatScreenPreview() {
    BubbyChatTheme {
        ChatScreen(onSendQuestion = {}, responseText = "")
    }
}