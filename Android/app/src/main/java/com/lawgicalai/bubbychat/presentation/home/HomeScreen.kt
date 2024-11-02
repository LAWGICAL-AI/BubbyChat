package com.lawgicalai.bubbychat.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGrayDark
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyLightOrange
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is HomeSideEffect.Toast -> {
                Toast.makeText(context, it.massage, Toast.LENGTH_SHORT).show()
            }
        }
    }
    HomeScreen(
        chatSessions = state.sessions,
        onSessionClick = {},
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    chatSessions: List<ChatSession>,
    onSessionClick: (ChatSession) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    BottomSheetScaffold(
        containerColor = Color.White,
        modifier = Modifier.background(Color.White),
        scaffoldState = scaffoldState,
        sheetContainerColor = Color.White,
        sheetContent = {
            ChatSessionList(
                chatSessions = chatSessions,
                onSessionClick = onSessionClick,
            )
        },
        sheetPeekHeight = 300.dp, // BottomSheet 기본 높이
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
        sheetShape = RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp),
        content = { padding ->
            // 메인 화면 콘텐츠
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(BubbyGreen.copy(alpha = 0.1f))
                        .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // 예: 이전 상담 내역 보기 버튼
                Button(
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.expand() // BottomSheet 확장
                        }
                    },
                ) {
                    Text("이전 상담 내역 보기")
                }
            }
        },
    )
}

@Composable
fun ChatSessionList(
    chatSessions: List<ChatSession>,
    onSessionClick: (ChatSession) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "이전 상담 내역",
            color = BubbyGrayDark,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp),
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier.fillMaxSize(),
        ) {
            items(chatSessions) { session ->
                ChatSessionCard(
                    chatSession = session,
                    onClick = { onSessionClick(session) },
                )
            }
        }
    }
}

@Composable
fun ChatSessionCard(
    chatSession: ChatSession,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(min = 180.dp, max = 180.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BubbyLightOrange)
                .clickable { onClick() }
                .padding(16.dp),
    ) {
        Column {
            Text(
                text = chatSession.text,
                color = Color.Black,
                style =
                    MaterialTheme.typography.titleLarge.copy(
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                maxLines = 1, // 최대 1줄로 제한
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = chatSession.firstResponse,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = chatSession.timestamp,
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    BubbyChatTheme {
        HomeScreen(
            emptyList(),
            {},
        )
    }
}
