package com.lawgicalai.bubbychat.presentation.chat

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.domain.model.Persona
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkerGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGreen
import com.lawgicalai.bubbychat.presentation.utils.noRippleClickable
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "PersonaScreen"

@Composable
fun PersonaScreen(
    viewModel: ChatViewModel,
    onPersonaSelected: () -> Unit,
) {
    PersonaScreen(onSavePersona = viewModel::selectPersona, onPersonaSelected = { personaType ->
        Timber.tag(TAG).d("personaType: $personaType")
        viewModel.selectPersona(personaType)
        onPersonaSelected()
    })
}

@Composable
private fun PersonaScreen(
    onSavePersona: (String) -> Unit,
    onPersonaSelected: (String) -> Unit,
) {
    val personas =
        listOf(
            Persona(type = "friendly", name = "가벼운 상담", description = "일반적인 법률 상담을 진행합니다"),
            Persona(type = "expert", name = "전문가 상담", description = "전문적인 법률 상담을 진행합니다"),
        )

    var selectedPersona by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
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
                MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
            modifier = Modifier.padding(vertical = 24.dp),
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            personas.forEach { persona ->
                val isSelected = selectedPersona == persona.type
                val scale by animateFloatAsState(
                    targetValue =
                        if (isSelected) {
                            1.1f
                        } else if (selectedPersona != null) {
                            0.8f
                        } else {
                            1f
                        },
                    animationSpec = tween(300),
                )
                val alpha by animateFloatAsState(
                    targetValue = if (selectedPersona == null || isSelected) 1f else 0.6f,
                    animationSpec = tween(300),
                    label = "",
                )

                PersonaCard(
                    isSelected = isSelected,
                    persona = persona,
                    modifier =
                        Modifier
                            .weight(1f)
                            .graphicsLayer {
                                scaleX = scale
                                scaleY = scale
                                this.alpha = alpha
                            },
                    onPersonaSelected = {
                        onSavePersona(persona.type)
                        selectedPersona = persona.type
                        coroutineScope.launch {
                            delay(300)
                        }
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedPersona?.let {
                    onSavePersona(it)
                    onPersonaSelected(it)
                } ?: run {
                    Toast.makeText(context, "상담 유형을 선택해주세요", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.padding(vertical = 12.dp),
            colors =
                ButtonDefaults.buttonColors(
                    containerColor =
                        BubbyDarkerGreen.copy(
                            alpha = 0.9f,
                        ),
                ),
        ) {
            Text(
                modifier = Modifier.padding(4.dp),
                text = "상담하기",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            )
        }
    }
}

@Composable
fun PersonaCard(
    isSelected: Boolean,
    persona: Persona,
    modifier: Modifier = Modifier,
    onPersonaSelected: () -> Unit,
) {
    val imageResource =
        if (persona.type == "friendly") {
            R.drawable.lawyer_easy
        } else {
            R.drawable.lawyer_hard
        }

    Card(
        modifier =
            modifier
                .fillMaxWidth()
                .background(Color.White)
                .aspectRatio(1f)
                .noRippleClickable {
                    onPersonaSelected()
                },
        shape = CircleShape,
        colors =
            CardDefaults.cardColors(
                containerColor =
                    if (isSelected) {
                        BubbyDarkerGreen.copy(alpha = 0.8f)
                    } else {
                        BubbyGreen.copy(
                            alpha = 0.1f,
                        )
                    },
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(BubbyGreen.copy(alpha = 0.1f))
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = persona.name,
                style =
                    MaterialTheme.typography.titleMedium.copy(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.White else Color.Black,
            )
            Box(modifier = Modifier.size(48.dp)) {
                Image(
                    painter = painterResource(imageResource),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
            Text(
                text = persona.description,
                style =
                    MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = if (isSelected) Color.White else Color.Gray,
            )
        }
    }
}

@Preview
@Composable
fun PersonaScreenPreview() {
    BubbyChatTheme {
        PersonaScreen(onSavePersona = {}, onPersonaSelected = { it -> })
    }
}

@Preview
@Composable
fun PersonaScreenPreview2() {
    BubbyChatTheme {
        PersonaCard(
            persona = Persona("friendly", "friendly", "friendly"),
            isSelected = true,
            onPersonaSelected = {},
        )
    }
}
