package io.github.junkfood.heal.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import io.github.junkfood.heal.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.heal.util.PreferenceUtil
import kotlinx.coroutines.flow.Flow

val LocalDarkTheme = compositionLocalOf { PreferenceUtil.DarkThemePreference() }

val LocalSeedColor = compositionLocalOf { DEFAULT_SEED_COLOR }

val settingFlow: Flow<PreferenceUtil.AppSettings> = PreferenceUtil.AppSettingsStateFlow

@Composable
fun SettingsProvider(content: @Composable () -> Unit) {
    val appSettingsState = settingFlow.collectAsState(PreferenceUtil.initialAppSettings()).value
    CompositionLocalProvider(
        LocalDarkTheme provides appSettingsState.darkTheme,
        LocalSeedColor provides appSettingsState.seedColor,
        content = content
    )
}