package io.github.junkfood.heal.util

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.tencent.mmkv.MMKV
import io.github.junkfood.heal.BaseApplication.Companion.applicationScope
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object PreferenceUtil {
    private val kv = MMKV.defaultMMKV()
    private const val maxHistoryAmount = 15

    data class AppSettings(
        val darkTheme: DarkThemePreference = DarkThemePreference(),
        val seedColor: Int = DEFAULT_SEED_COLOR
    )

    const val DARK_THEME = "dark_theme_value"

    private const val THEME_COLOR = "theme_color"

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

    }

    private val mutableAppSettingsStateFlow = MutableStateFlow(
        AppSettings(
            DarkThemePreference(
                kv.decodeInt(
                    DARK_THEME,
                    DarkThemePreference.FOLLOW_SYSTEM
                )
            ), kv.decodeInt(THEME_COLOR, DEFAULT_SEED_COLOR)
        )
    )
    val AppSettingsStateFlow = mutableAppSettingsStateFlow.asStateFlow()

    fun modifyThemeSeedColor(colorArgb: Int) {
        applicationScope.launch(Dispatchers.IO) {
            mutableAppSettingsStateFlow.update {
                it.copy(seedColor = colorArgb)
            }
            kv.encode(THEME_COLOR, colorArgb)
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

//    suspend fun insertHistory(id: Long) {
//        if (kv.decodeStringSet("history") == null || kv.decodeStringSet("history")!!.size == 0) {
//            val set = LinkedHashSet<String>()
//            set.add(id.toString())
//            kv.encode("history", set)
//        } else {
//        val set = kv.decodeStringSet("history") as LinkedHashSet<String>
//        if (set.size < maxHistoryAmount) {
//            if (!set.add(id.toString())) {
//                set.remove(id.toString())
//                set.add(id.toString())
//            }
//        }
//        else {
//            if (set.remove(id.toString())) {
//                set.add(id.toString())
//            } else {
//                set.remove(set.iterator().next())
//                set.add(id.toString())
//            }
//        }
//
//        kv.encode("history", set)
//        set.clear()
//    }
//    }
//
//    fun getHistory(): List<Long> {
//        val idList = arrayOf<Long>()
//        val set = kv.decodeStringSet("history", setOf())
//        if (set != null) {
//            for (item in set) {
//                idList[idList.size] = item.toLong()
//            }
//        }
//        idList.reverse()
//        return idList.toList()
//    }
}