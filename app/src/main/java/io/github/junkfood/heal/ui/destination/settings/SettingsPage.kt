package io.github.junkfood.heal.ui.destination.settings

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.sharp.Aod
import androidx.compose.material.icons.sharp.Download
import androidx.compose.material.icons.sharp.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.material.color.DynamicColors
import io.github.junkfood.heal.ui.color.hct.Hct
import io.github.junkfood.heal.ui.color.palettes.CorePalette
import io.github.junkfood.heal.ui.common.LocalDarkTheme
import io.github.junkfood.heal.ui.common.LocalSeedColor
import io.github.junkfood.heal.ui.component.BackButton
import io.github.junkfood.heal.ui.theme.ColorScheme
import io.github.junkfood.heal.util.PreferenceUtil

@Composable
fun SettingsPage(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.statusBarsPadding()
        ) {
            SmallTopAppBar(
                title = {},
                navigationIcon = { BackButton { navController.popBackStack() } },
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                modifier = Modifier.padding(start = 24.dp, top = 48.dp),
                text = "设置",
                style = MaterialTheme.typography.headlineLarge
            )
            Column(modifier = Modifier.padding(top = 24.dp)) {
                SettingItem(
                    title = "下载设置",
                    description = "",
                    icon = Icons.Sharp.Download
                ) {
                }
                SettingItem(
                    title = "播放设置",
                    description = "",
                    icon = Icons.Sharp.Aod
                ) {
                }
                SettingItem(
                    title = "关于",
                    description = "",
                    icon = Icons.Sharp.Info
                ) {
                }
                Text(
                    text = "主题颜色",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(12.dp)
                )
                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(12.dp)
                ) {
                    if (DynamicColors.isDynamicColorAvailable()) {
                        ColorButton(
                            color = dynamicDarkColorScheme(
                                LocalContext.current
                            ).primary
                        )
                        ColorButton(
                            color = dynamicDarkColorScheme(
                                LocalContext.current
                            ).tertiary
                        )
                    }
                    ColorButton(
                        color = Color(
                            ColorScheme.DEFAULT_SEED_COLOR
                        )
                    )
                    ColorButton(color = Color.Yellow)
                    ColorButton(
                        color = Color(
                            Hct.from(
                                60.0,
                                150.0,
                                70.0
                            ).toInt()
                        )
                    )
                    ColorButton(
                        color = Color(
                            Hct.from(
                                125.0,
                                50.0,
                                60.0
                            ).toInt()
                        )
                    )
                    ColorButton(color = Color.Red)
                    ColorButton(color = Color.Magenta)
                    ColorButton(color = Color.Blue)
                }
            }
        }
    }
}

@Composable
fun SettingItem(title: String, description: String, icon: ImageVector?, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icon?.let {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier
                        .padding(start = 8.dp, end = 16.dp)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
            }
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = if (icon == null) 12.dp else 0.dp)
            ) {
                Text(
                    text = title,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = description,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 1,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

        }
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
        .size(72.dp), onClick = { PreferenceUtil.modifyThemeColor(seedColor) }) {
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