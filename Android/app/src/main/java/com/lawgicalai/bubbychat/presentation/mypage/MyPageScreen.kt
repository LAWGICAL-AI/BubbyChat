package com.lawgicalai.bubbychat.presentation.mypage

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.lawgicalai.bubbychat.domain.model.User
import com.lawgicalai.bubbychat.presentation.anim.WavyAnimation
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyDarkerGreen
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyGrayDark
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun MyPageScreen(myPageViewModel: MyPageViewModel) {
    val state = myPageViewModel.collectAsState().value
    val context = LocalContext.current
    val activity = context as? Activity

    myPageViewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is MyPageSideEffect.Toast -> {
                Toast.makeText(context, sideEffect.message, Toast.LENGTH_SHORT).show()
            }

            is MyPageSideEffect.Restart -> {
                activity?.finishAffinity()
                val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
                context.startActivity(intent)
            }
        }
    }
    MyPageScreen(
        user = state.userInfo,
        onSignOutClick = myPageViewModel::signOut,
    )
}

@Composable
fun MyPageScreen(
    user: User,
    onSignOutClick: () -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier =
            Modifier
                .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box {
            WavyAnimation(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .rotate(180f)
                        .height(184.dp),
            )
            WavyAnimation(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .rotate(180f)
                        .height(184.dp),
                wavelength = 300f,
            )
        }

        Image(
            painter = rememberAsyncImagePainter(model = user.profileImage),
            contentDescription = "Profile Image",
            modifier =
                Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .border(
                        width = 4.dp,
                        color = BubbyDarkerGreen.copy(alpha = 0.2f),
                        shape = CircleShape,
                    ),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.displayName ?: "",
            style =
                MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                ),
        )

        Text(
            text = user.email ?: "",
            style =
                MaterialTheme.typography.bodyLarge.copy(
                    color = BubbyGrayDark,
                ),
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth().padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray.copy(alpha = 0.5f))
                    .padding(16.dp),
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    modifier =
                        Modifier.clickable {
                            uriHandler.openUri(
                                "https://docs.google.com/document/d/1Sl9NQqC_KApRxAfUOadV7CA3OpJ8oO3LwzZoE6NHkUY/edit?usp=sharing",
                            )
                        },
                    text = "개인 정보 처리 방침",
                    style =
                        MaterialTheme.typography.titleMedium.copy(
                            color = BubbyDarkerGreen,
                            fontWeight = FontWeight.Bold,
                        ),
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = onSignOutClick,
                    modifier = Modifier.widthIn(min = 120.dp),
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor = BubbyDarkerGreen.copy(alpha = 0.9f),
                        ),
                ) {
                    Text(
                        text = "로그아웃",
                        style =
                            MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                            ),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "버전: 1.0.0",
            style =
                MaterialTheme.typography.bodySmall.copy(
                    color = BubbyGrayDark,
                ),
            modifier = Modifier.padding(bottom = 8.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MyPageScreenPreview() {
    BubbyChatTheme {
        MyPageScreen(
            user =
                User(
                    email = "william.henry.harrison@example-pet-store.com",
                    displayName = "John Doe",
                    profileImage = null,
                ),
            onSignOutClick = {},
        )
    }
}
