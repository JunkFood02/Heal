package io.github.junkfood.heal.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.*
import io.github.junkfood.heal.ui.common.NavigationGraph.EPISODE_ID
import io.github.junkfood.heal.ui.common.NavigationGraph.PODCAST_ID
import io.github.junkfood.heal.ui.common.NavigationGraph.withArgument
import io.github.junkfood.heal.ui.destination.episode.EpisodePage
import io.github.junkfood.heal.ui.destination.feed.FeedPage
import io.github.junkfood.heal.ui.destination.feed.FeedViewModel
import io.github.junkfood.heal.ui.destination.library.LibraryPage
import io.github.junkfood.heal.ui.destination.library.LibraryViewModel
import io.github.junkfood.heal.ui.destination.listen.ListenPage
import io.github.junkfood.heal.ui.destination.podcast.PodcastPage
import io.github.junkfood.heal.ui.destination.settings.SettingsPage
import io.github.junkfood.heal.ui.theme.PodcastTheme

private const val TAG = "HomeEntry"

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeEntry() {

    SettingsProvider {
        PodcastTheme(
            darkTheme = LocalDarkTheme.current.isDarkTheme(),
            seedColor = LocalSeedColor.current
        ) {

            val navController = rememberAnimatedNavController()
            val onBackPressed = { navController.popBackStack() }
            var selectedTab by remember { mutableStateOf(0) }
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                selectedTab = navController.getTopDestinationRoute()
            }
            Scaffold(modifier = Modifier.padding(
                bottom = WindowInsets.systemBars
                    .asPaddingValues()
                    .calculateBottomPadding()
            ), bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = selectedTab == 0,
                        onClick = {
                            navController.navigate(NavigationGraph.FEED) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                if (selectedTab != 0) Icons.Outlined.Home else Icons.Filled.Home,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.home)) },
                    )
                    NavigationBarItem(
                        selected = selectedTab == 1,
                        onClick = {
                            navController.navigate(NavigationGraph.LISTEN) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                if (selectedTab != 1) Icons.Outlined.PlayArrow else Icons.Filled.PlayArrow,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.listen)) },
                    )
                    NavigationBarItem(
                        selected = selectedTab == 2,
                        onClick = {
                            navController.navigate(NavigationGraph.LIBRARY) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                if (selectedTab != 2) Icons.Outlined.CollectionsBookmark else Icons.Filled.CollectionsBookmark,
                                contentDescription = null
                            )
                        },
                        label = { Text(stringResource(R.string.library)) },
                    )
                }
            }, content = { paddingValues ->
                val feedViewModel = FeedViewModel()
                val libraryViewModel = LibraryViewModel()
                AnimatedNavHost(
                    modifier = Modifier.padding(paddingValues),
                    navController = navController,
                    startDestination = NavigationGraph.FEED
                ) {
                    fadedComposable(NavigationGraph.FEED) {
                        FeedPage(
                            navController,
                            feedViewModel
                        )
                    }
                    fadedComposable(NavigationGraph.LISTEN) {
                        ListenPage(navController)
                    }
                    fadedComposable(NavigationGraph.LIBRARY) {
                        LibraryPage(navController, libraryViewModel)
                    }
                    animatedComposable(NavigationGraph.SETTINGS) {
                        SettingsPage(navController)
                    }

                    animatedComposable(
                        NavigationGraph.EPISODE.withArgument(EPISODE_ID),
                        arguments = listOf(navArgument(EPISODE_ID) {
                            type = NavType.LongType
                        })
                    ) { backStackEntry ->
                        EpisodePage(
                            navController,
                            backStackEntry.arguments?.getLong(EPISODE_ID) ?: 0
                        )
                    }
                    animatedComposable(
                        NavigationGraph.PODCAST.withArgument(PODCAST_ID),
                        arguments = listOf(navArgument(PODCAST_ID) {
                            type = NavType.LongType
                        })
                    ) { backStackEntry ->
                        PodcastPage(
                            navController,
                            backStackEntry.arguments?.getLong(PODCAST_ID) ?: 0
                        )
                    }
                }


            })
        }
    }
}

fun NavHostController.getTopDestinationRoute(): Int {
    this.backQueue.reversed().forEach {
        Log.d(TAG, it.destination.route.toString())
        when (it.destination.route) {
            NavigationGraph.FEED -> return 0
            NavigationGraph.LISTEN -> return 1
            NavigationGraph.LIBRARY -> return 2
        }
    }
    return 0
}