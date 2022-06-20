package io.github.junkfood.podcast.ui.destination

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.android.material.color.DynamicColors
import io.github.junkfood.podcast.ui.color.hct.Hct
import io.github.junkfood.podcast.ui.color.palettes.CorePalette
import io.github.junkfood.podcast.ui.common.LocalDarkTheme
import io.github.junkfood.podcast.ui.common.LocalSeedColor
import io.github.junkfood.podcast.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.podcast.util.PreferenceUtil.modifyThemeColor

@Composable
fun AppearanceSettings() {
    Row(
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        if (DynamicColors.isDynamicColorAvailable()) {
            ColorButton(color = dynamicDarkColorScheme(LocalContext.current).primary)
            ColorButton(color = dynamicDarkColorScheme(LocalContext.current).tertiary)
        }
        ColorButton(color = Color(DEFAULT_SEED_COLOR))
        ColorButton(color = Color.Yellow)
        ColorButton(color = Color(Hct.from(60.0, 150.0, 70.0).toInt()))
        ColorButton(color = Color(Hct.from(125.0, 50.0, 60.0).toInt()))
        ColorButton(color = Color.Red)
        ColorButton(color = Color.Magenta)
        ColorButton(color = Color.Blue)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorButton(modifier: Modifier = Modifier, color: Color) {
    val corePalette = CorePalette.of(color.toArgb())
    val lightColor = corePalette.a2.tone(80)
    val seedColor = corePalette.a2.tone(80)
    val darkColor = corePalette.a2.tone(60)

    val showColor = if (LocalDarkTheme.current.isDarkTheme()) darkColor else lightColor
    val currentColor = LocalSeedColor.current == seedColor
    val state = animateDpAsState(targetValue = if (currentColor) 48.dp else 36.dp)
    val state2 = animateDpAsState(targetValue = if (currentColor) 18.dp else 0.dp)
    ElevatedCard(modifier = modifier
        .padding(4.dp)
        .size(72.dp), onClick = { modifyThemeColor(seedColor) }) {
        Box(Modifier.fillMaxSize()) {
            Box(
                modifier = modifier
                    .size(state.value)
                    .clip(CircleShape)
                    .background(Color(showColor))
                    .align(Alignment.Center)
            ) {

                Icon(
                    Icons.Outlined.Check,
                    null,
                    modifier = Modifier
                        .size(state2.value)
                        .align(Alignment.Center)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

}