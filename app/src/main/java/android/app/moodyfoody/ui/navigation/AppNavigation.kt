package android.app.moodyfoody.ui.navigation

import android.app.moodyfoody.ui.destinations.Destinations
import android.app.moodyfoody.ui.screens.FavoritesScreen
import android.app.moodyfoody.ui.screens.HomeScreen
import android.app.moodyfoody.ui.screens.LoadingScreen
import android.app.moodyfoody.ui.screens.LoginScreen
import android.app.moodyfoody.ui.screens.PreferencesSetupScreen
import android.app.moodyfoody.ui.screens.ProfileScreen
import android.app.moodyfoody.ui.screens.RecipeDetailScreen
import android.app.moodyfoody.ui.screens.SettingsScreen
import android.app.moodyfoody.ui.screens.SplashScreen
import android.app.moodyfoody.ui.screens.WelcomeScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(
    startDestination: Destinations
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(0))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(0))
        }
    ) {
        composable<Destinations.SplashScreen> { SplashScreen() }
        composable<Destinations.FavoritesScreen> { FavoritesScreen() }
        composable<Destinations.HomeScreen> { HomeScreen() }
        composable<Destinations.LoginScreen> { LoginScreen() }
        composable<Destinations.PreferencesSetupScreen> { PreferencesSetupScreen() }
        composable<Destinations.ProfileScreen> { ProfileScreen() }

        // Figure out how to navigate to each id recipe detail screen
        composable<Destinations.RecipeDetailScreen> { RecipeDetailScreen() }

        composable<Destinations.SettingsScreen> { SettingsScreen() }
        composable<Destinations.WelcomeScreen> { WelcomeScreen() }
        composable<Destinations.LoadingScreen> { LoadingScreen() }
    }
}