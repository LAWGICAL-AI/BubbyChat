package com.lawgicalai.bubbychat.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.domain.model.Persona
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun PersonaScreen(
    viewModel: ChatViewModel = hiltViewModel(),
    onPersonaSelected: (String) -> Unit,
) {
    val state = viewModel.collectAsState().value
    PersonaScreen(onPersonaSelected = onPersonaSelected)
}

@Composable
private fun PersonaScreen(onPersonaSelected: (String) -> Unit) {
    val personas =
        listOf(
            Persona(type = "friendly", name = "가벼운 상담", description = "일반적인 법률 상담을 진행합니다."),
            Persona(type = "expert", name = "전문가 상담", description = "전문적인 상담을 진행합니다."),
        )

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "상담 유형을 선택해 주세요",
            style =
                MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.padding(vertical = 24.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))
        // 이거는 transition 사용해서 전환되는 animation 넣을 예정
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(personas) { persona ->
                PersonaCard(
                    persona = persona,
                    onPersonaSelected = {
                        onPersonaSelected(persona.type)
                    },
                )
            }
        }
    }
}

@Composable
fun PersonaCard(
    persona: Persona,
    onPersonaSelected: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onPersonaSelected() },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            modifier =
                Modifier
                    .background(BubbyGreen.copy(alpha = 0.1f))
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = persona.name,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = persona.description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun PersonaScreenPreview() {
    BubbyChatTheme {
        PersonaScreen(onPersonaSelected = {})
    }
}
