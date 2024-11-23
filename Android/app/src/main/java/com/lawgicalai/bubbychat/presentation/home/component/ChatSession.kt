package com.lawgicalai.bubbychat.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lawgicalai.bubbychat.domain.model.ChatSession
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGrayDark
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyLightOrange

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
            text = "대화 목록",
            color = BubbyGrayDark,
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(bottom = 8.dp),
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
                modifier = Modifier.fillMaxSize().padding(top = 20.dp),
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
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}
