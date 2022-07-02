package io.github.junkfood.heal.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import io.github.junkfood.heal.BaseApplication.Companion.context
import io.github.junkfood.heal.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.heal.util.PreferenceUtil
import io.github.junkfood.heal.util.PreferenceUtil.DarkThemePreference.Companion.FOLLOW_SYSTEM
import io.github.junkfood.heal.util.PreferenceUtil.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val LocalDarkTheme = compositionLocalOf { PreferenceUtil.DarkThemePreference() }

val LocalSeedColor = compositionLocalOf { DEFAULT_SEED_COLOR }

val settingFlow: Flow<PreferenceUtil.AppSettings> =
    context.dataStore.data.map {
        PreferenceUtil.AppSettings(
            PreferenceUtil.DarkThemePreference(it[PreferenceUtil.DARK_THEME_KEY] ?: FOLLOW_SYSTEM),
            it[PreferenceUtil.THEME_COLOR_KEY] ?: DEFAULT_SEED_COLOR
        )
    }

@Composable
fun SettingsProvider(content: @Composable () -> Unit) {
    val appSettingsState = settingFlow.collectAsState(PreferenceUtil.initialAppSettings()).value
    CompositionLocalProvider(
        LocalDarkTheme provides appSettingsState.darkTheme,
        LocalSeedColor provides appSettingsState.seedColor,
        content = content
    )
}