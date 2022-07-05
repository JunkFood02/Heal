package io.github.junkfood.heal.ui.destination.settings

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.rounded.DownloadForOffline
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.PlaylistAdd
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.android.material.color.DynamicColors
import io.github.junkfood.heal.MainActivity
import io.github.junkfood.heal.ui.common.LocalDarkTheme
import io.github.junkfood.heal.ui.common.LocalNavHostController
import io.github.junkfood.heal.util.PreferenceUtil
import io.github.junkfood.heal.util.PreferenceUtil.LANGUAGE
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.color.hct.Hct
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.*
import io.github.junkfood.heal.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.heal.util.PreferenceUtil.DarkThemePreference.Companion.FOLLOW_SYSTEM
import io.github.junkfood.heal.util.PreferenceUtil.DarkThemePreference.Companion.OFF
import io.github.junkfood.heal.util.PreferenceUtil.DarkThemePreference.Companion.ON
import io.github.junkfood.heal.util.PreferenceUtil.getLanguageConfiguration
import io.github.junkfood.heal.util.TextUtil
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearancePreferences(navHostController: NavHostController = LocalNavHostController.current) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    var showDarkThemeDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    val darkTheme = LocalDarkTheme.current
    var darkThemeValue by remember { mutableStateOf(darkTheme.darkThemeValue) }
    var language by remember { mutableStateOf(PreferenceUtil.getInt(LANGUAGE, 0)) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            io.github.junkfood.heal.ui.component.LargeTopAppBar(
                title = {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(id = R.string.user_interface),
                    )
                }, navigationIcon = {
                    IconButton(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = { navHostController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }, scrollBehavior = scrollBehavior
            )
        }, content = {
            Column(
                Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                EpisodePagePreview()

/*                var showcase by remember { mutableStateOf(false) }
                PreferenceSwitch(
                    title = stringResource(R.string.color_theming),
                    icon = Icons.Outlined.ColorLens,
                    description = stringResource(R.string.color_theming_desc),
                    onClick = { showcase = !showcase },
                    isChecked = showcase
                )*/
                Column {
                    Row(
                        modifier = Modifier
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 9.dp)
                            .padding(bottom = 6.dp)
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

                PreferenceItem(
                    title = stringResource(id = R.string.dark_theme),
                    description = LocalDarkTheme.current.getDarkThemeDesc(),
                    icon = Icons.Outlined.DarkMode,
                    enabled = true
                ) { showDarkThemeDialog = true }
                PreferenceItem(
                    title = stringResource(R.string.interface_language),
                    icon = Icons.Outlined.Language,
                    description = PreferenceUtil.getLanguageDesc()
                ) { showLanguageDialog = true }
            }
        })
    if (showDarkThemeDialog)
        AlertDialog(onDismissRequest = {
            showDarkThemeDialog = false
            darkThemeValue = darkTheme.darkThemeValue
        }, confirmButton = {
            ConfirmButton {
                showDarkThemeDialog = false
                PreferenceUtil.switchDarkThemeMode(darkThemeValue)
            }
        }, dismissButton = {
            DismissButton {
                showDarkThemeDialog = false
                darkThemeValue = darkTheme.darkThemeValue
            }
        }, title = { Text(stringResource(R.string.dark_theme)) }, text = {
            Column() {
                SingleChoiceItem(
                    text = stringResource(R.string.follow_system),
                    selected = darkThemeValue == FOLLOW_SYSTEM
                ) {
                    darkThemeValue = FOLLOW_SYSTEM
                }
                SingleChoiceItem(
                    text = stringResource(androidx.compose.ui.R.string.on),
                    selected = darkThemeValue == ON
                ) {
                    darkThemeValue = ON
                }
                SingleChoiceItem(
                    text = stringResource(androidx.compose.ui.R.string.off),
                    selected = darkThemeValue == OFF
                ) {
                    darkThemeValue = OFF
                }
            }
        })

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = {
                showLanguageDialog = false
                language = PreferenceUtil.getInt(LANGUAGE, 0)
            },
            confirmButton = {
                ConfirmButton {
                    showLanguageDialog = false
                    PreferenceUtil.updateInt(LANGUAGE, language)
                    MainActivity.setLanguage(getLanguageConfiguration())
                }
            }, dismissButton = {
                DismissButton {
                    showLanguageDialog = false
                    language = PreferenceUtil.getInt(LANGUAGE, 0)
                }
            },
            title = { Text(stringResource(R.string.interface_language)) }, text = {
                Column {
                    SingleChoiceItem(
                        text = stringResource(R.string.la_en_US),
                        selected = language == 2
                    ) { language = 2 }
                    SingleChoiceItem(
                        text = stringResource(R.string.la_zh_CN),
                        selected = language == 1
                    ) { language = 1 }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun EpisodePagePreview() {

}

