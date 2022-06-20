package io.github.junkfood.podcast.ui.destination

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.android.material.color.DynamicColors
import io.github.junkfood.podcast.R
import io.github.junkfood.podcast.ui.color.hct.Hct
import io.github.junkfood.podcast.ui.color.palettes.CorePalette
import io.github.junkfood.podcast.ui.common.LocalDarkTheme
import io.github.junkfood.podcast.ui.common.LocalSeedColor
import io.github.junkfood.podcast.ui.theme.ColorScheme.DEFAULT_SEED_COLOR
import io.github.junkfood.podcast.util.PreferenceUtil.modifyThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings() {
    Column(
        Modifier
            .systemBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(12.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(5f, matchHeightConstraintsFirst = true)
                    .height(IntrinsicSize.Min)
            ) {
                AsyncImage(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.small)
                        .aspectRatio(1f, matchHeightConstraintsFirst = true),
                    model = R.drawable.cover,
                    contentDescription = null
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 6.dp)
                        .fillMaxHeight(), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        "【随机波动070】想象一种更好的社交网络，也是想象更好的社会",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        "随机波动StochasticVolatility",
                        modifier = Modifier.padding(top = 3.dp),
                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }
            Text(
                "作为社交网络重度成瘾者，这期节目像是一个小规模的戒酒会，参与者先要忏悔：我叫xxx，我上周的平均屏幕使用时间是6小时49分钟，其中5小时23分钟用于社交网络，微信、微博和小红书瓜分了我一天生命中的五个小时，令我效率低下、情绪波动、颈椎僵直、眼睛干涩。",
                style = MaterialTheme.typography.bodySmall,
//                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(top = 6.dp)
            )

        }
        Row(Modifier.padding(horizontal = 6.dp)) {
            ElevatedCard(modifier = Modifier
                .weight(1f)
                .padding(6.dp), onClick = {}) { CardContent() }

            Card(modifier = Modifier
                .weight(1f)
                .padding(6.dp), onClick = {}) { CardContent() }
        }
        Row(Modifier.padding(horizontal = 6.dp)) {
            OutlinedCard(modifier = Modifier
                .weight(1f)
                .padding(6.dp), onClick = {}) { CardContent() }
            Column(modifier = Modifier
                .weight(1f)
                .padding(6.dp)
                .clip(MaterialTheme.shapes.medium)
                .clickable { }) {
                CardContent()
            }
        }
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(12.dp)
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

@Composable
fun CardContent() {

    AsyncImage(
        modifier = Modifier
            .clip(MaterialTheme.shapes.medium)
            .aspectRatio(1f, matchHeightConstraintsFirst = true),
        model = R.drawable.cover,
        contentDescription = null
    )
    Text(
        "【随机波动070】想象一种更好的社交网络，也是想象更好的社会",
        modifier = Modifier.padding(
            top = 9.dp,
            bottom = 3.dp,
            start = 12.dp,
            end = 12.dp
        ),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
    Text(
        "随机波动StochasticVolatility",
        modifier = Modifier.padding(bottom = 15.dp, start = 12.dp, end = 12.dp),
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

}