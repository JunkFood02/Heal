package io.github.junkfood.podcast.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.podcast.ui.common.*
import io.github.junkfood.podcast.ui.destination.FeedPage
import io.github.junkfood.podcast.ui.destination.FeedViewModel
import io.github.junkfood.podcast.ui.destination.episode.EpisodePage
import io.github.junkfood.podcast.ui.theme.PodcastTheme

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeEntry() {

    SettingsProvider {
        PodcastTheme(
            darkTheme = LocalDarkTheme.current.isDarkTheme(),
            seedColor = LocalSeedColor.current
        ) {
            val navController = rememberAnimatedNavController()
            val onBackPressed = { navController.popBackStack() }
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                val feedViewModel: FeedViewModel = hiltViewModel()
                AnimatedNavHost(
                    modifier = Modifier,
                    navController = navController,
                    startDestination = RouteName.FEED
                ) {
                    animatedComposable(RouteName.FEED) { FeedPage(navController, feedViewModel) }
                    animatedComposable(RouteName.EPISODE) {
                        EpisodePage(
                            navController,
                            feedViewModel
                        )
                    }
                }
            }
        }
    }
}