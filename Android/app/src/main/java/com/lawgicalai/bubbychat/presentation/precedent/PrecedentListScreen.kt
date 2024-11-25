package com.lawgicalai.bubbychat.presentation.precedent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.domain.model.Precedent
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PrecedentListScreen(
    viewModel: PrecedentViewModel = hiltViewModel(),
    onPrecedentClick: (String) -> Unit,
) {
    val state = viewModel.collectAsState().value
    PrecedentListScreen(onPrecedentClick = onPrecedentClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrecedentListScreen(
    onPrecedentClick: (String) -> Unit,
    searchQuery: String,
    selectedCategory: String?,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    precedents: List<Precedent>,
    isLoading: Boolean,
) {
    var searchExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarColors(
                    containerColor = BubbyGreen,
                    scrolledContainerColor = BubbyGreen,
                    navigationIconContentColor = Color.Black,
                    titleContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                title = { Text("판례 검색") },
                actions = {
                    IconButton(onClick = { searchExpanded = !searchExpanded }) {
                        Icon(
                            imageVector = if (searchExpanded) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "검색",
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            // 카테고리 필터
            val categories =
                listOf(
                    "전체",
                    "민사",
                    "형사",
                    "행정",
                    "가사",
                    "특허",
                    "세무",
                    "노동",
                    "헌법",
                    "상사",
                    "환경",
                )

            LazyRow(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(categories) { category ->
                    FilterChip(
                        selected = category == selectedCategory,
                        onClick = { onCategorySelected(category) },
                        label = { Text(category) },
                        colors =
                            FilterChipDefaults.filterChipColors(
                                selectedContainerColor = BubbyGreen,
                                selectedLabelColor = Color.White,
                            ),
                    )
                }
            }

            // 판례 목록
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(precedents) { precedent ->
                    PrecedentCard(
                        precedent = precedent,
                        onClick = { onPrecedentClick(precedent.id) },
                    )
                }
            }
        }

        // 로딩 인디케이터
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun PrecedentCard(
    precedent: Precedent,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
        ) {
            Text(
                text = precedent.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "${precedent.court} | ${precedent.caseNumber}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = precedent.date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = precedent.summary,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 8.dp),
            )
            // 키워드 칩
            LazyRow(
                modifier = Modifier.padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                items(precedent.keywords) { keyword ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(keyword) },
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PrecedentScreenPreview() {
    BubbyChatTheme {
        PrecedentListScreen(
            onPrecedentClick = {},
            searchQuery = "solet",
            selectedCategory = null,
            onQueryChange = {},
            onCategorySelected = {},
            precedents = listOf(),
            isLoading = false
        )
    }
}
