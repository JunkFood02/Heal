package io.github.junkfood.heal.ui.destination.listen

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons


import androidx.compose.material.icons.outlined.MoreVert

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import io.github.junkfood.heal.R
import io.github.junkfood.heal.ui.common.LocalNavHostController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun ListenPage(navHostController: NavHostController = LocalNavHostController.current) {
    Scaffold(
        modifier = Modifier
            .padding()
            .fillMaxSize(), topBar = {
            io.github.junkfood.heal.ui.component.SmallTopAppBar(actions = {
                IconButton(

                    onClick = {}) {
                    Icon(Icons.Outlined.MoreVert, null)

                }
            })
        }, content = {
            Column(modifier = Modifier.padding(it)) {
                Column(modifier = Modifier.weight(7f), verticalArrangement = Arrangement.Center) {
                    AsyncImage(
                        modifier = Modifier
                            .aspectRatio(1f, true)
                            .padding(24.dp)
                            .clip(MaterialTheme.shapes.large),
                        model = R.drawable.sample,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                    )
                }
                Column(
                    modifier = Modifier.weight(3f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp),
                        text = "Episode Title very very long!!",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(text = "Episode Title very very long!!")

                }
            }
        }
    )
}