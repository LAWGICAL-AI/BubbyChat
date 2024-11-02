import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@Composable
fun Record(
    modifier: Modifier = Modifier,
    onSendQuestion: (String) -> Unit,
) {
    val context = LocalContext.current
    var isListening by remember { mutableStateOf(false) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO,
            ) == PackageManager.PERMISSION_GRANTED,
        )
    }
    var recognizedText by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    // 권한 요청 런처
    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { granted ->
            hasPermission = granted
            if (granted) {
                isListening = true
                showDialog = true
            }
        }

    Icon(
        painter =
            painterResource(
                id = if (isListening) R.drawable.ic_record else R.drawable.ic_mic,
            ),
        contentDescription = "mic",
        modifier =
            modifier
                .background(BubbyDarkGreen, shape = RoundedCornerShape(10.dp))
                .clickable {
                    if (hasPermission) {
                        isListening = !isListening // 음성 인식 시작 또는 중지
                        showDialog = isListening // 인식 중 다이얼로그 표시
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }.padding(8.dp),
        tint = if (isListening) Color.Red else Color.White,
    )

    // 음성 인식 기능을 포함한 Record 호출
    if (isListening) {
        Record(
            isListening = isListening,
            onResult = { resultText ->
                recognizedText = resultText
                showDialog = false // 인식 완료 후 다이얼로그 닫기
                isListening = false
                if (recognizedText.isNotEmpty()) {
                    onSendQuestion(recognizedText)
                    recognizedText = ""
                }
            },
            onCancel = {
                // 취소 요청 시 상태 변경 및 인식 중단
                isListening = false
                showDialog = false
                recognizedText = ""
            },
            onPartialResult = { partialText ->
                recognizedText = partialText // 실시간으로 다이얼로그 텍스트 업데이트
            },
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                showDialog = false
                isListening = false
            },
            confirmButton = {
                Text(
                    text = "취소하기",
                    modifier =
                        Modifier.clickable {
                            showDialog = false
                            isListening = false
                        },
                    style = MaterialTheme.typography.labelMedium,
                )
            },
            title = {
                Text(
                    text = "궁금한걸 물어보세요",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center,
                )
            },
            text = {
                Text(
                    modifier =
                        Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                            .background(
                                shape = RoundedCornerShape(8.dp),
                                color = Color.LightGray.copy(alpha = 0.3f),
                            ).padding(8.dp),
                    text = recognizedText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            },
            containerColor = Color.White,
        )
    }
}

@Composable
private fun Record(
    isListening: Boolean,
    onResult: (String) -> Unit,
    onCancel: () -> Unit,
    onPartialResult: (String) -> Unit,
) {
    val context = LocalContext.current
    val speechRecognizer = remember { SpeechRecognizer.createSpeechRecognizer(context) }
    val coroutineScope = rememberCoroutineScope()

    val speechIntent =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.KOREAN)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }

    val recognitionListener =
        object : RecognitionListener {
            override fun onResults(results: Bundle?) {
                coroutineScope.launch {
                    delay(2000) // 2초 뒤에 꺼지도록
                    val response =
                        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()
                            ?: ""
                    onResult(response)
                }
            }

            override fun onError(error: Int) {
                onResult("")
            }

            override fun onReadyForSpeech(params: Bundle?) {}

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onPartialResults(partialResults: Bundle?) {
                // 부분 결과가 있을 때 다이얼로그에 실시간 업데이트
                val partialText =
                    partialResults
                        ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        ?.firstOrNull() ?: ""
                Timber.tag("STT").d("partialText: $partialText")
                onPartialResult(partialText)
            }

            override fun onEvent(
                eventType: Int,
                params: Bundle?,
            ) {
            }
        }

    DisposableEffect(isListening) {
        if (isListening) {
            speechRecognizer.setRecognitionListener(recognitionListener)
            speechRecognizer.startListening(speechIntent)
        } else {
            speechRecognizer.stopListening()
        }

        onDispose {
            speechRecognizer.destroy()
        }
    }

    LaunchedEffect(isListening) {
        if (!isListening) {
            onCancel()
        }
    }
}
