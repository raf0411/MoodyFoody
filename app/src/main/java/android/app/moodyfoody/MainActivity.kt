package android.app.moodyfoody

import android.app.moodyfoody.data.model.UserData
import android.app.moodyfoody.data.repository.AuthRepository
import android.app.moodyfoody.data.repository.FirestoreRepository
import android.app.moodyfoody.ui.destinations.Destinations
import android.app.moodyfoody.ui.navigation.AppNavigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.app.moodyfoody.ui.theme.MoodyFoodyTheme
import android.app.moodyfoody.viewModel.SplashViewModel
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()

    var startDestination = Destinations.WelcomeScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { splashViewModel.isLoading.value }
        enableEdgeToEdge()

        setContent {
            MoodyFoodyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Box(
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        AppNavigation(
                            startDestination = startDestination
                        )
                    }
                }
            }
        }
    }
}