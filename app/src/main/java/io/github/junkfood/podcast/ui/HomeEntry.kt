package io.github.junkfood.podcast.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.podcast.ui.common.*
import io.github.junkfood.podcast.ui.common.NavigationUtil.EPISODE_ID
import io.github.junkfood.podcast.ui.common.NavigationUtil.withArgument
import io.github.junkfood.podcast.ui.destination.feed.FeedPage
import io.github.junkfood.podcast.ui.destination.feed.FeedViewModel
import io.github.junkfood.podcast.ui.destination.podcast.PodcastPage
import io.github.junkfood.podcast.ui.destination.episode.EpisodePage
import io.github.junkfood.podcast.ui.destination.library.LibraryPage
import io.github.junkfood.podcast.ui.destination.library.LibraryViewModel
import io.github.junkfood.podcast.ui.destination.settings.SettingsPage
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
                    
                    startDestination = NavigationUtil.FEED
                ) {
                    animatedComposable(NavigationUtil.FEED) { FeedPage(navController, feedViewModel) }
                    animatedComposable(
                        NavigationUtil.EPISODE.withArgument(EPISODE_ID),
                        arguments = listOf(navArgument(EPISODE_ID) { type = NavType.LongType })
                    ) { backStackEntry ->
                        EpisodePage(
                            navController,
                            backStackEntry.arguments?.getLong(EPISODE_ID) ?: 0
                        )
                    }
                    animatedComposable(NavigationUtil.PODCAST) {
                        PodcastPage(feedViewModel, navController)
                    }
                    animatedComposable(NavigationUtil.LIBRARY) {
                        LibraryPage(navController, libraryViewModel)
                    }
                    animatedComposable(NavigationUtil.SETTINGS) {
                        SettingsPage(navController)
                    }
                }
            }
        }
    }
}