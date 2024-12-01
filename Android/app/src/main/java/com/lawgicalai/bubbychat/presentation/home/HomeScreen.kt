package com.lawgicalai.bubbychat.presentation.home

import android.widget.Toast
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.StartOffsetType
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.presentation.anim.WavyAnimation
import com.lawgicalai.bubbychat.presentation.home.component.ChatSessionList
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkerGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGrayDark
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onStartClick: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navController = rememberNavController()

    LaunchedEffect(Unit) {
        // navController 추적
        navController.currentBackStackEntryFlow.collect { entry ->
            if (entry.savedStateHandle.contains("refresh")) {
                entry.savedStateHandle.remove<Boolean>("refresh")
                viewModel.getAllSessions()
            }
        }
    }

    if (state.isShowDialog) {
        ChatHistoryDialog(
            onDismiss = viewModel::hideSessionDetail,
            chatMessages = state.selectedSession,
        )
    }

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Toast -> {
                Toast.makeText(context, it.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    HomeScreen(
        userInfo = state.userInfo,
        chatSessions = state.sessions,
        onSessionClick = { it ->
            viewModel.getChatSession(it.sessionId)
            viewModel.showSessionDetail()
        },
        onStartClick = onStartClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    userInfo: User,
    chatSessions: List<ChatSession>,
    onSessionClick: (ChatSession) -> Unit,
    onStartClick: () -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val peekHeight = screenHeight * 0.2f
    BottomSheetScaffold(
        containerColor = BubbyGreen.copy(alpha = 0.1f),
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetShadowElevation = 0.dp,
        sheetTonalElevation = 0.dp,
        sheetShape = RoundedCornerShape(20.dp),
        sheetContent = {
            Box(
                modifier =
                    Modifier
                        // 화면 높이의 70%로 제한 (bottombar 위치까지)
                        .height(screenHeight * 0.7f),
            ) {
                ChatSessionList(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                    chatSessions = chatSessions,
                    onSessionClick = onSessionClick,
                )
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(80.dp) // BottomBar 높이
                            .background(Color.White.copy(alpha = 0.01f))
                            .align(Alignment.BottomCenter),
                )
            }
        },
        sheetPeekHeight = peekHeight,
        sheetDragHandle = {
            Box(
                modifier =
                    Modifier
                        .padding(4.dp)
                        .background(BubbyGrayDark)
                        .clip(RoundedCornerShape(10.dp))
                        .height(4.dp)
                        .width(20.dp),
            )
        },
        content = { padding ->
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box {
                    WavyAnimation(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .rotate(180f)
                                .height(184.dp),
                    )
                    WavyAnimation(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .rotate(180f)
                                .height(184.dp),
                        wavelength = 300f,
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_playstore),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .border(
                                width = 4.dp,
                                color = BubbyDarkerGreen.copy(alpha = 0.2f),
                                shape = CircleShape,
                            ),
                )
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "안녕하세요, ${userInfo.displayName}님",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        )
                        Image(
                            painter = rememberAsyncImagePainter(model = userInfo.profileImage),
                            contentDescription = "profileImage",
                            modifier = Modifier.size(32.dp).padding(start = 4.dp),
                        )
                    }

                    Text(text = "버비와 법률관련 대화를 시작해보세요", style = MaterialTheme.typography.bodyLarge)
                }
                CarouselText()
                Button(
                    onClick = onStartClick,
                    modifier = Modifier.padding(vertical = 8.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                BubbyDarkerGreen.copy(
                                    alpha = 0.9f,
                                ),
                        ),
                ) {
                    Text(
                        text = "\uD83E\uDD5A시작하기\uD83D\uDC25",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Text(
                    modifier = Modifier.padding(bottom = 8.dp),
                    text = "AI의 정보는 틀릴 수 있습니다. 중요한 정보는 검증과정이 필요합니다",
                    style = MaterialTheme.typography.bodySmall.copy(color = BubbyGrayDark),
                )
            }
        },
    )
}

@Composable
fun CarouselText() {
    val messages =
        listOf(
            "회사에서 부당해고를 당했다고 생각하는데 어떻게 해야할까요?",
            "특허 출원을 하려고 하는데 어떤 과정을 거쳐야 하나요?",
            "임대차 계약을 해지하고 싶은데 어떤 절차를 밟아야 하나요?",
            "임금체불이 발생했는데 어떻게 대응해야 할까요?",
            "건설 중인 아파트의 분양계약을 해제하려면 어떻게 해야 할까요?",
        )

    var currentIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(5000) // 6초마다 텍스트 변경 fade-in,out 6초
            currentIndex = (currentIndex + 1) % messages.size
        }
    }

    val transition = rememberInfiniteTransition(label = "")
    val alpha by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec =
            InfiniteRepeatableSpec(
                animation = tween(2500), // tween 이니까 한 텍스트에 6초
                RepeatMode.Reverse,
                StartOffset(0, StartOffsetType.FastForward),
            ),
        label = "FloatAnimation",
    )

    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFEFEFEF))
                .padding(horizontal = 16.dp, vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = messages[currentIndex],
            fontSize = 16.sp,
            color = Color.DarkGray.copy(alpha = alpha),
            maxLines = 1,
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    BubbyChatTheme {
        HomeScreen(
            userInfo = User(email = null, displayName = null, profileImage = null),
            emptyList(),
            {},
            {},
        )
    }
}
