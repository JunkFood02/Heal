package io.github.junkfood.podcast.util

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.tencent.mmkv.MMKV
import io.github.junkfood.podcast.BaseApplication.Companion.applicationScope
import io.github.junkfood.podcast.BaseApplication.Companion.context
import io.github.junkfood.podcast.R
import io.github.junkfood.podcast.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object PreferenceUtil {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val kv = MMKV.defaultMMKV()

    data class AppSettings(
        val darkTheme: DarkThemePreference = DarkThemePreference(),
        val seedColor: Int = DEFAULT_SEED_COLOR
    )


    const val DARK_THEME = "dark_theme_value"

    private const val THEME_COLOR = "theme_color"
    val DARK_THEME_KEY = intPreferencesKey(DARK_THEME)
    val THEME_COLOR_KEY = intPreferencesKey(THEME_COLOR)

    fun modifyThemeColor(colorArgb: Int) {
        applicationScope.launch {
            context.dataStore.edit { settings ->
                settings[THEME_COLOR_KEY] = colorArgb
                kv.encode(THEME_COLOR, colorArgb)
            }
        }
    }

    class DarkThemePreference(var darkThemeValue: Int = FOLLOW_SYSTEM) {
        companion object {
            const val FOLLOW_SYSTEM = 1
            const val ON = 2
            const val OFF = 3
        }

        @Composable
        fun isDarkTheme(): Boolean {
            return if (darkThemeValue == FOLLOW_SYSTEM)
                isSystemInDarkTheme()
            else darkThemeValue == ON
        }

        @Composable
        fun getDarkThemeDesc(): String {
            return when (darkThemeValue) {
                FOLLOW_SYSTEM -> stringResource(R.string.follow_system)
                ON -> stringResource(R.string.on)
                else -> stringResource(R.string.off)
            }
        }

        fun switch(value: Int) {
            darkThemeValue = value
            applicationScope.launch(Dispatchers.IO) {
                kv.encode(DARK_THEME, value)
                context.dataStore.edit { settings ->
                    settings[DARK_THEME_KEY] = value
                }
            }
        }
    }

    @Composable
    fun initialAppSettings(): AppSettings {
        return AppSettings(
            DarkThemePreference(
                kv.decodeInt(
                    DARK_THEME,
                    DarkThemePreference.FOLLOW_SYSTEM
                )
            ), kv.decodeInt(THEME_COLOR, DEFAULT_SEED_COLOR)
        )
    }
}