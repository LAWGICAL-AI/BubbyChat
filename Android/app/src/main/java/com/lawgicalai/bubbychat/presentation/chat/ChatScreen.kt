package com.lawgicalai.bubbychat.presentation.chat

import Record
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.domain.model.ChatMessage
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyLightOrange
import com.lawgicalai.bubbychat.presentation.ui.theme.Typography
import com.lawgicalai.bubbychat.presentation.utils.noRippleClickable
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import timber.log.Timber

private const val TAG = "ChatScreen"

@Composable
fun ChatScreen(viewModel: ChatViewModel) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val ttsManager = remember { TextToSpeechManager(context) }
    var isDialogVisible by remember { mutableStateOf(false) }

    viewModel.collectSideEffect {
        when (it) {
            is ChatSideEffect.Toast -> {
                Toast.makeText(context, it.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    if (state.isShowDialog) {
        PrecedentDetailDialog(
            onDismiss = viewModel::hidePrecedentDetail,
            precedent = state.selectedPrecedent,
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            Timber.tag(TAG).d("onDispose")
            viewModel.saveMessages()
            ttsManager.stop()
        }
    }

    ChatScreen(
        personaType = state.personaType,
        onSendQuestion = viewModel::getResponse,
        onInputTextChange = viewModel::textInputChange,
        inputText = state.input,
        messages = state.messages,
        isResponseComplete = state.isResponseComplete,
        resetResponse = viewModel::resetResponse,
        onLongClickSpeak = {
            ttsManager.speak(it)
            isDialogVisible = true
        },
        onNavigateToPrecedentScreen = viewModel::showPrecedentDetail,
    )

    val composition by rememberLottieComposition(
        spec =
            LottieCompositionSpec.RawRes(
                R.raw.anim_speak,
            ),
    )
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )

    if (isDialogVisible) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                isDialogVisible = false
                ttsManager.stop() // 다이얼로그가 닫힐 때 TTS 중지
            },
            icon = {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(100.dp),
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "메시지를 읽고 있습니다...",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    )
                }
            },
            confirmButton = {
                Text(
                    text = "닫기",
                    modifier =
                        Modifier.clickable {
                            isDialogVisible = false
                            ttsManager.stop()
                        },
                )
            },
        )
    }
}

@Composable
private fun ChatScreen(
    personaType: String,
    onSendQuestion: (String) -> Unit,
    onInputTextChange: (String) -> Unit,
    inputText: String,
    messages: List<ChatMessage>,
    isResponseComplete: Boolean,
    resetResponse: () -> Unit,
    onLongClickSpeak: (String) -> Unit,
    onNavigateToPrecedentScreen: () -> Unit,
) {
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current

    // Composable에서 isResponseComplete가 true일 때만 스크롤
    LaunchedEffect(messages, isResponseComplete) {
        if (isResponseComplete && messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
            resetResponse()
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
                    .background(Color.White),
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
                    ChatBubble(
                        message,
                        onLongClickSpeak,
                        onNavigateToPrecedentScreen = onNavigateToPrecedentScreen,
                        personaType,
                    )
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
        Icon(
            painter = painterResource(id = R.drawable.ic_send),
            contentDescription = "Send",
            modifier =
                Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .rotate(90f)
                    .background(BubbyGreen, shape = RoundedCornerShape(10.dp))
                    .clickable {
                        onSendQuestion(inputText)
                        focusManager.clearFocus()
                    }.padding(6.dp),
            tint = Color.White,
        )
        Record(
            modifier =
                Modifier
                    .size(40.dp)
                    .aspectRatio(1f)
                    .padding(start = 4.dp),
            onSendQuestion = onSendQuestion,
        )
    }
}

@Composable
fun ChatBubble(
    message: ChatMessage,
    onLongClickSpeak: (String) -> Unit,
    onNavigateToPrecedentScreen: () -> Unit,
    personaType: String,
) {
    val backgroundColor = if (message.isMine) BubbyLightOrange else BubbyGreen
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalAlignment = if (message.isMine) Alignment.End else Alignment.Start,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = if (message.isMine) Arrangement.End else Arrangement.Start,
        ) {
            Box(
                modifier =
                    Modifier
                        .background(backgroundColor, shape = RoundedCornerShape(12.dp))
                        .padding(8.dp)
                        .widthIn(max = 250.dp)
                        .clickable {
                            onLongClickSpeak(message.text) // 길게 누르면 speak 호출
                        },
            ) {
                Text(text = message.text, fontSize = 16.sp)
            }
        }

        if (!message.isMine && personaType == "expert") {
            // 이건 조건 하나 더 달아아 됨 -> 판례데이터를 가져오는 데 성공했다면
            Row(
                modifier =
                    Modifier
                        .padding(top = 4.dp),
                // 버튼 그룹의 최대 너비 제한
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                NavigationButton(
                    onClick = onNavigateToPrecedentScreen,
                )
            }
        }
    }
}

@Composable
private fun NavigationButton(onClick: () -> Unit) {
    Box(
        modifier =
            Modifier
                .background(
                    color = BubbyDarkGreen,
                    shape = RoundedCornerShape(8.dp),
                ).noRippleClickable(onClick = onClick)
                .padding(vertical = 8.dp, horizontal = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "↪ 자세히 알아보기",
            color = Color.Black,
            style = Typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
        )
    }
}

@Preview
@Composable
fun ChatScreenPreview() {
    BubbyChatTheme {
        ChatScreen(
            personaType = "friendly",
            onSendQuestion = {},
            onInputTextChange = {},
            inputText = "",
            messages = emptyList(),
            isResponseComplete = false,
            resetResponse = {},
            onLongClickSpeak = {},
            onNavigateToPrecedentScreen = {},
        )
    }
}

@Preview
@Composable
fun ChatBubblePreview() {
    BubbyChatTheme {
        ChatBubble(
            message = ChatMessage(text = "ffffffffffffffff", isMine = false),
            onLongClickSpeak = {},
            onNavigateToPrecedentScreen = {},
            personaType = "expert",
        )
    }
}
