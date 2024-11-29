package com.lawgicalai.bubbychat.presentation.chat

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.lawgicalai.bubbychat.domain.model.Precedent
import com.lawgicalai.bubbychat.domain.model.PrecedentBody
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import com.lawgicalai.bubbychat.presentation.utils.noRippleClickable

@Composable
fun PrecedentDetailDialog(
    onDismiss: () -> Unit,
    precedent: List<PrecedentBody.Precedent>,
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
                        text = "관련 판례",
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
                    items(precedent) {
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
                                with(it) {
                                    case_name.takeIf { it.isNotBlank() }?.let {
                                        InfoRow("사건명", it)
                                    }
                                    case_number.takeIf { it.isNotBlank() }?.let {
                                        InfoRow("사건번호", it)
                                    }
                                    case_type.takeIf { it.isNotBlank() }?.let {
                                        InfoRow("분류", it)
                                    }
                                    ref_article.takeIf { it.isNotBlank() }?.let {
                                        InfoRow("상세", it, url)
                                    }
                                }
                            }
                        }
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
    url: String? = null,
) {
    val context = LocalContext.current
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
    ) {
        Text(
            text = "📢 " + label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (!url.isNullOrBlank()) {
            // URL이 있는 경우 클릭 가능한 텍스트로 표시
            Text(
                text = value,
                style =
                    MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.primary,
                    ),
                modifier =
                    Modifier
                        .padding(top = 4.dp, bottom = 8.dp)
                        .noRippleClickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
            )
        } else {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            )
        }
    }
}

@Preview
@Composable
fun PrecedentDetailDialogPreview() {
    BubbyChatTheme {
        PrecedentDetailDialog(
            onDismiss = {},
            precedent =
                listOf(
                    PrecedentBody.Precedent(
                        case_name = "Cleo Richmond",
                        case_number = "prompta",
                        case_type = "accommodare",
                        ref_article = "pro",
                        url = "http://www.bing.com/search?q=dolorem",
                    ),
                ),
        )
    }
}
