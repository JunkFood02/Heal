package io.github.junkfood.podcast.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.podcast.ui.common.*
import io.github.junkfood.podcast.ui.destination.feed.FeedPage
import io.github.junkfood.podcast.ui.destination.feed.FeedViewModel
import io.github.junkfood.podcast.ui.destination.podcast.PodcastPage
import io.github.junkfood.podcast.ui.destination.episode.EpisodePage
import io.github.junkfood.podcast.ui.destination.library.LibraryPage
import io.github.junkfood.podcast.ui.destination.library.LibraryViewModel
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
                color = MaterialTheme.colorScheme.surface
            ) {
                val feedViewModel: FeedViewModel = hiltViewModel()
                val libraryViewModel: LibraryViewModel = hiltViewModel()
                AnimatedNavHost(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            bottom = WindowInsets.systemBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    navController = navController,
                    startDestination = RouteName.LIBRARY//test
                ) {
                    animatedComposable(RouteName.FEED) { FeedPage(navController, feedViewModel) }
                    animatedComposable(RouteName.EPISODE) {
                        EpisodePage(
                            navController,
                            feedViewModel
                        )
                    }
                    animatedComposable(RouteName.PODCAST) {
                        PodcastPage(feedViewModel, navController)
                    }
                    animatedComposable(RouteName.LIBRARY) {
                        LibraryPage(navController, libraryViewModel)
                    }
                }
            }
        }
    }
}