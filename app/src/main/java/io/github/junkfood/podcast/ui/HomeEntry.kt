package io.github.junkfood.podcast.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.podcast.ui.common.*
import io.github.junkfood.podcast.ui.destination.AppearanceSettings
import io.github.junkfood.podcast.ui.theme.PodcastTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeEntry() {
    val navController = rememberAnimatedNavController()
    val onBackPressed = { navController.popBackStack() }
    SettingsProvider {
        PodcastTheme(
            darkTheme = LocalDarkTheme.current.isDarkTheme(),
            seedColor = LocalSeedColor.current
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                AnimatedNavHost(navController = navController, startDestination = RouteName.COLOR) {
                    animatedComposable(RouteName.COLOR) { AppearanceSettings() }
                }
            }
        }
    }
}