package com.lawgicalai.bubbychat.presentation.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lawgicalai.bubbychat.domain.model.Precedent
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen

@Composable
fun PrecedentDetailDialog(
    onDismiss: () -> Unit,
    precedent: Precedent,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties =
            DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = true,
                dismissOnClickOutside = true,
                decorFitsSystemWindows = false,
            ),
    ) {
        Surface(
            modifier =
                Modifier
                    .fillMaxSize(),
            color = Color.White,
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.KeyboardArrowLeft, "닫기")
                    }
                    Text(
                        text = precedent.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                LazyColumn(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    item {
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
fun PrecedentDetailDialogPreview() {
    BubbyChatTheme {
        PrecedentDetailDialog(
            onDismiss = {},
            precedent =
                Precedent(
                    id = "prompta",
                    title = "tota",
                    court = "idque",
                    caseNumber = "unum",
                    date = "conubia",
                    summary = "oratio",
                    content = "mus",
                    category = "nibh",
                    relatedLaws = listOf(),
                    keywords = listOf(),
                ),
        )
    }
}
