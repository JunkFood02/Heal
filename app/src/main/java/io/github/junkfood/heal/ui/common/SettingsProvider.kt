package io.github.junkfood.heal.ui.common

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import io.github.junkfood.heal.BaseApplication.Companion.context
import io.github.junkfood.heal.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.heal.util.PreferenceUtil
import kotlinx.coroutines.flow.Flow

val LocalDarkTheme = compositionLocalOf { PreferenceUtil.DarkThemePreference() }
val LocalSeedColor = compositionLocalOf { DEFAULT_SEED_COLOR }
val LocalNavHostController = staticCompositionLocalOf { NavHostController(context) }
val settingFlow: Flow<PreferenceUtil.AppSettings> = PreferenceUtil.AppSettingsStateFlow

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SettingsProvider(content: @Composable () -> Unit) {
    val appSettingsState = settingFlow.collectAsState(PreferenceUtil.initialAppSettings()).value
    CompositionLocalProvider(
        LocalDarkTheme provides appSettingsState.darkTheme,
        LocalSeedColor provides appSettingsState.seedColor,
        LocalNavHostController provides rememberAnimatedNavController(),
        content = content
    )
}