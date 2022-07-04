package io.github.junkfood.heal.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import io.github.junkfood.heal.ui.theme.ColorScheme.darkColorSchemeFromColor
import io.github.junkfood.heal.ui.theme.ColorScheme.lightColorSchemeFromColor

fun Color.applyOpacity(enabled: Boolean): Color {
    return if (enabled) this else this.copy(alpha = 0.62f)
}

@Composable
fun PodcastTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    seedColor: Int,
    content: @Composable () -> Unit
) {

    rememberSystemUiController().run {
        setStatusBarColor(Color.Transparent, !darkTheme)
        setSystemBarsColor(Color.Transparent, !darkTheme)
        setNavigationBarColor(Color.Transparent, !darkTheme)
    }

    val colorScheme =
        when {
            darkTheme -> darkColorSchemeFromColor(seedColor)
            else -> lightColorSchemeFromColor(seedColor)
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}