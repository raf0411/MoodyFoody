package android.app.moodyfoody.ui.screens

import android.app.moodyfoody.viewModel.AuthViewModel
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel = hiltViewModel()
) {
    // Simple email/anonymous login (can be combined with onboarding)
    Text("Login Screen")
}