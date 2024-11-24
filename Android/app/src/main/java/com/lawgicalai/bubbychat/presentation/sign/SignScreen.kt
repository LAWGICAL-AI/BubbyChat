package com.lawgicalai.bubbychat.presentation.sign

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.lawgicalai.bubbychat.R
import com.lawgicalai.bubbychat.presentation.ui.theme.BubbyChatTheme
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun SignScreen(
    viewModel: SignViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
) {
    val state = viewModel.collectAsState().value
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    LaunchedEffect(state.userInfo) {
        if (state.userInfo.email != null) {
            onLoginSuccess()
        }
    }
    SignScreen(onGoogleSignInClick = viewModel::googleSignIn)
}

@Composable
private fun SignScreen(onGoogleSignInClick: () -> Unit) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // 로고 이미지
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_round),
            contentDescription = "App Logo",
            modifier = Modifier.size(100.dp),
        )

        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 12.dp),
        ) {
            Text(
                text = "안녕하세요.",
                style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "서비스 이용을 위해 로그인 해주세요.",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
            )
        }

        OutlinedButton(
            onClick = onGoogleSignInClick,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(48.dp),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            colors =
                ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                ),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                AsyncImage(
                    model = com.google.firebase.appcheck.interop.R.drawable.common_google_signin_btn_icon_light_normal,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Google 계정으로 로그인",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview
@Composable
fun SignScreenPreview() {
    BubbyChatTheme {
        SignScreen(onGoogleSignInClick = {})
    }
}
