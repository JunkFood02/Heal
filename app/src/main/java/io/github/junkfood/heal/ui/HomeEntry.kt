package io.github.junkfood.heal.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.heal.ui.common.*
import io.github.junkfood.heal.ui.common.NavigationUtil.EPISODE_ID
import io.github.junkfood.heal.ui.common.NavigationUtil.PODCAST_ID
import io.github.junkfood.heal.ui.common.NavigationUtil.withArgument
import io.github.junkfood.heal.ui.destination.episode.EpisodePage
import io.github.junkfood.heal.ui.destination.feed.FeedPage
import io.github.junkfood.heal.ui.destination.feed.FeedViewModel
import io.github.junkfood.heal.ui.destination.library.LibraryPage
import io.github.junkfood.heal.ui.destination.library.LibraryViewModel
import io.github.junkfood.heal.ui.destination.podcast.PodcastPage
import io.github.junkfood.heal.ui.destination.settings.SettingsPage
import io.github.junkfood.heal.ui.theme.PodcastTheme

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
                val feedViewModel = FeedViewModel()
                val libraryViewModel = LibraryViewModel()
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
                    animatedComposable(NavigationUtil.FEED) {
                        FeedPage(
                            navController,
                            feedViewModel
                        )
                    }
                    animatedComposable(
                        NavigationUtil.EPISODE.withArgument(EPISODE_ID),
                        arguments = listOf(navArgument(EPISODE_ID) { type = NavType.LongType })
                    ) { backStackEntry ->
                        EpisodePage(
                            navController,
                            backStackEntry.arguments?.getLong(EPISODE_ID) ?: 0
                        )
                    }
                    animatedComposable(
                        NavigationUtil.PODCAST.withArgument(PODCAST_ID),
                        arguments = listOf(navArgument(PODCAST_ID) { type = NavType.LongType })
                    ) { backStackEntry ->
                        PodcastPage(
                            navController,
                            backStackEntry.arguments?.getLong(PODCAST_ID) ?: 0
                        )
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