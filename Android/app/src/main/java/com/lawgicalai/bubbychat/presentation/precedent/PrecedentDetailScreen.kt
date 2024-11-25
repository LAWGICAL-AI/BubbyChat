package com.lawgicalai.bubbychat.presentation.precedent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.domain.model.Precedent
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PrecedentDetailScreen(
    viewModel: PrecedentViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val state = viewModel.collectAsState().value

    PrecedentDetailScreen(
        onBackClick = {},
        precedent =
            Precedent(
                id = "legere",
                title = "hinc",
                court = "malorum",
                caseNumber = "mollis",
                date = "nunc",
                summary = "ac",
                content = "bibendum",
                category = "inani",
                relatedLaws = listOf(),
                keywords = listOf(),
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PrecedentDetailScreen(
    onBackClick: () -> Unit,
    precedent: Precedent,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = BubbyGreen,
                    scrolledContainerColor = BubbyGreen,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = BubbyGreen
                ),
                title = { Text(precedent.title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "뒤로가기")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(top = 12.dp)
                    .padding(padding)
                    .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                // 기본 정보
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                        CardDefaults.cardColors(
                            containerColor = BubbyGreen.copy(alpha = 0.6f),
                        ),
                ) {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        InfoRow("법원", precedent.court)
                        InfoRow("사건번호", precedent.caseNumber)
                        InfoRow("선고일자", precedent.date)
                        InfoRow("분류", precedent.category)
                    }
                }
            }

            // 관련 법령
            item {
                Text(
                    "관련 법령",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Column {
                    precedent.relatedLaws.forEach { law ->
                        Text(
                            text = "• $law",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(vertical = 4.dp),
                        )
                    }
                }
            }

            // 판결 요지
            item {
                Text(
                    "판결 요지",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = precedent.summary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            // 판결 전문
            item {
                Text(
                    "판결 전문",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = precedent.content,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
fun PrecedentDetailScreenPreview() {
    BubbyChatTheme {
        PrecedentDetailScreen(onBackClick = {}, precedent = Precedent(
            id = "prompta",
            title = "tota",
            court = "idque",
            caseNumber = "unum",
            date = "conubia",
            summary = "oratio",
            content = "mus",
            category = "nibh",
            relatedLaws = listOf(),
            keywords = listOf()
        ))
    }
}
