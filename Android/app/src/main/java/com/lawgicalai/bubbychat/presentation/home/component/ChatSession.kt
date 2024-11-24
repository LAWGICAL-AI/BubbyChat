package com.lawgicalai.bubbychat.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGrayDark
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyLightOrange
import com.lawgicalai.bubbychat.presentation.utils.noRippleClickable

@Composable
fun ChatSessionList(
    modifier: Modifier = Modifier,
    chatSessions: List<ChatSession>,
    onSessionClick: (ChatSession) -> Unit,
) {
    val gridState = rememberLazyGridState()
    val nestedScrollConnection =
        remember {
            object : NestedScrollConnection {
                override fun onPreScroll(
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset =
                    if (source == NestedScrollSource.Drag) {
                        // 스크롤이 최상단이고 위로 드래그하는 경우
                        if (gridState.firstVisibleItemIndex == 0 && available.y > 0) {
                            Offset.Zero // 부모에게 스크롤 이벤트 전달
                        } else if (!gridState.canScrollForward && available.y < 0) {
                            // 스크롤이 최하단이고 아래로 드래그하는 경우
                            Offset.Zero // 부모에게 스크롤 이벤트 전달
                        } else {
                            // 그 외의 경우 자식이 스크롤 이벤트 처리
                            available.copy(x = 0f)
                        }
                    } else {
                        Offset.Zero
                    }

                override suspend fun onPreFling(available: Velocity): Velocity {
                    // fling 동작 처리
                    return if (gridState.firstVisibleItemIndex == 0 && available.y > 0) {
                        available // 최상단에서 위로 플링하는 경우 부모에게 전달
                    } else if (!gridState.canScrollForward && available.y < 0) {
                        available // 최하단에서 아래로 플링하는 경우 부모에게 전달
                    } else {
                        Velocity.Zero // 그 외에는 자식이 처리
                    }
                }

                override suspend fun onPostFling(
                    consumed: Velocity,
                    available: Velocity,
                ): Velocity {
                    return available // 남은 fling 속도를 부모에게 전달
                }
            }
        }

    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
    ) {
        Text(
            text = "대화 목록",
            color = BubbyGrayDark,
            style =
                MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.padding(bottom = 4.dp),
        )

        if (chatSessions.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = "이전 상담 내역이 없습니다",
                    color = BubbyGrayDark,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp),
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                state = gridState,
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(top = 12.dp)
                        .nestedScroll(nestedScrollConnection),
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
}

@Composable
fun ChatSessionCard(
    chatSession: ChatSession,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .padding(8.dp)
                .heightIn(min = 160.dp, max = 160.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BubbyLightOrange)
                .noRippleClickable { onClick() }
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
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}
