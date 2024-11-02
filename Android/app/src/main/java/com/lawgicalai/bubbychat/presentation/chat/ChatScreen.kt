package com.lawgicalai.bubbychat.presentation.chat

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
fun ChatScreen(viewModel: ChatViewModel = hiltViewModel()) {
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
        messages = state.messages,
    )
}

@Composable
private fun ChatScreen(
    onSendQuestion: (String) -> Unit,
    onInputTextChange: (String) -> Unit,
    inputText: String,
    messages: List<ChatMessage>,
) {
    val scrollState = rememberScrollState()
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    // 메시지가 추가되거나 마지막 메시지가 업데이트될 때마다 스크롤을 맨 아래로 이동
    LaunchedEffect(messages) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Surface(
        modifier =
            Modifier
                .background(Color.White)
                .clickable(
                    indication = null, // 리플 효과 제거
                    interactionSource = remember { MutableInteractionSource() },
                ) { focusManager.clearFocus() },
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .verticalScroll(scrollState),
        ) {
            Header()
            LazyColumn(
                modifier =
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .padding(bottom = 4.dp)
                        .background(Color.White),
                state = listState,
            ) {
                items(messages) { message ->
                    ChatBubble(message)
                }
            }
            InputTextField(
                inputText = inputText,
                onInputTextChange = onInputTextChange,
                onSendQuestion = onSendQuestion,
                focusManager = focusManager,
            )
        }
    }
}

@Composable
fun Header() {
    Column {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .background(BubbyGreen),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                modifier =
                    Modifier
                        .width(48.dp)
                        .aspectRatio(1f),
                painter = painterResource(id = R.drawable.ic_launcher_playstore),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = "버비가 법률 관련 상담을 도와드려요",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = "ex. 임금체불은 어떻게 해야될까?",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
fun InputTextField(
    inputText: String,
    onInputTextChange: (String) -> Unit,
    onSendQuestion: (String) -> Unit,
    focusManager: FocusManager,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TextField(
            modifier =
                Modifier
                    .weight(6f)
                    .padding(end = 6.dp),
            shape = RoundedCornerShape(12.dp),
            value = inputText,
            textStyle = MaterialTheme.typography.bodyLarge,
            onValueChange = onInputTextChange,
            colors =
                TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.LightGray.copy(alpha = 0.2f),
                    focusedContainerColor = Color.LightGray.copy(alpha = 0.4f),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    cursorColor = Color.Black,
                ),
        )
        IconButton(
            onClick = {
                onSendQuestion(inputText)
                focusManager.clearFocus()
            },
            modifier =
                Modifier
                    .weight(1f)
                    .padding(vertical = 2.dp)
                    .aspectRatio(1f)
                    .background(BubbyGreen, shape = RoundedCornerShape(10.dp)),
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_send),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.White, // 아이콘 색상 설정
            )
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val backgroundColor = if (message.isMine) Color(0xFFDCF8C6) else Color(0xFFECECEC)

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
    ) {
        Box(
            modifier =
                Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp)
                    .widthIn(max = 250.dp),
        ) {
            Text(text = message.text, fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    BubbyChatTheme {
        ChatScreen(
            onSendQuestion = {},
            onInputTextChange = {},
            inputText = "",
            messages = emptyList(),
        )
    }
}
