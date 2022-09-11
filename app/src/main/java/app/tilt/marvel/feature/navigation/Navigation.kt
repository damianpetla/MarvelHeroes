@file:OptIn(ExperimentalComposeUiApi::class)

package app.tilt.marvel.feature.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.rememberNavController
import app.tilt.marvel.feature.herocollection.HeroCollectionScreen
import app.tilt.marvel.feature.herodetails.HeroDetailsScreen

@Composable
fun MainNavigation() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.HeroCollection
        ) {
            composable(NavigationRoutes.HeroCollection) {
                HeroCollectionScreen { hero ->
                    navController.navigate(NavigationRoutes.heroDetailsParam(hero.id))
                }
            }

            dialog(
                NavigationRoutes.HeroDetails,
                dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
            ) {
                HeroDetailsScreen()
            }
        }
    }
}
