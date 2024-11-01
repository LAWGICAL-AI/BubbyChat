package com.lawgicalai.bubbychat.presentation.chat

import android.graphics.drawable.shapes.RectShape
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
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
    ChatScreen(
        onSendQuestion = viewModel::getResponse,
        onInputTextChange = viewModel::textInputChange,
        inputText = state.input,
        responseText = state.response
    )
}

@Composable
private fun ChatScreen(
    onSendQuestion: (String) -> Unit,
    onInputTextChange: (String) -> Unit,
    inputText: String,
    responseText: String
) {
    val scrollState = rememberScrollState()
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .verticalScroll(scrollState)
        ) {
            Text(modifier = Modifier.fillMaxWidth(), text = responseText)
            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = Modifier.weight(5f),
                    shape = RoundedCornerShape(12.dp),
                    value = inputText,
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 16.sp,
                    ),
                    onValueChange = onInputTextChange,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                        focusedContainerColor = Color.LightGray.copy(alpha = 0.6f),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        cursorColor = Color.Black
                    )
                )
                IconButton(
                    onClick = {
                        onSendQuestion("How to put an elephant into refrigerator")
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .weight(1f)
                        .aspectRatio(1f)
                        .background(BubbyGreen, shape = RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_send),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.White // 아이콘 색상 설정
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    BubbyChatTheme {
        ChatScreen(onSendQuestion = {}, onInputTextChange = {}, inputText = "", responseText = "")
    }
}