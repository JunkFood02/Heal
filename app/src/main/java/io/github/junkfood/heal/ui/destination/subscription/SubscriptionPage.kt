package io.github.junkfood.heal.ui.destination.subscription

import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.LocalNavHostController
import io.github.junkfood.heal.ui.common.NavigationGraph
import io.github.junkfood.heal.ui.common.NavigationGraph.toId
import io.github.junkfood.heal.ui.component.BackButton
import io.github.junkfood.heal.ui.component.LargeTopAppBar
import io.github.junkfood.heal.ui.destination.library.CardContent
import java.util.concurrent.Flow.Subscription

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionPage(
    navController: NavController = LocalNavHostController.current,
    subscriptionViewModel: SubscriptionViewModel = viewModel()
) {
    val viewState = subscriptionViewModel.subscriptionFlow.collectAsState(ArrayList()).value
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        decayAnimationSpec,
        rememberTopAppBarScrollState()
    )
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
        LargeTopAppBar(
            title = { Text(stringResource(id = R.string.subscriptions)) },
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackButton { navController.popBackStack() } },
        )
    }, content = {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 18.dp).padding(top = 18.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(viewState) { index, item ->
                SubscriptionCard(
                    imageModel = item.podcast.coverUrl,
                    title = item.podcast.title,
                    episodeCount = item.episodes.size
                ) {
                    navController.navigate(NavigationGraph.PODCAST.toId(item.podcast.id))
                }
            }
        }
    }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubscriptionCard(imageModel: Any, title: String, episodeCount: Int, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 6.dp), onClick = onClick
    ) {
        AsyncImage(
            modifier = Modifier
                .clip(MaterialTheme.shapes.medium)
                .aspectRatio(1f, matchHeightConstraintsFirst = true),
            model = imageModel,
            contentDescription = null
        )
        Text(
            title,
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
            stringResource(
                R.string.episodes
            ).format(episodeCount),
            modifier = Modifier.padding(bottom = 15.dp, start = 12.dp, end = 12.dp),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}