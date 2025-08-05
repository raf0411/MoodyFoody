package android.app.moodyfoody.ui.destinations

import kotlinx.serialization.Serializable

sealed class Destinations {
    @Serializable
    data object WelcomeScreen: Destinations()

    @Serializable
    data object HomeScreen: Destinations()

    @Serializable
    data object LoginScreen: Destinations()

    @Serializable
    data object PreferencesSetupScreen: Destinations()

    @Serializable
    data object SplashScreen: Destinations()

    @Serializable
    data object FavoritesScreen: Destinations()

    @Serializable
    data object LoadingScreen: Destinations()

    @Serializable
    data object ProfileScreen: Destinations()

    @Serializable
    data object RecipeDetailScreen: Destinations()

    @Serializable
    data object SettingsScreen: Destinations()
}