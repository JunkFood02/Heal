package io.github.junkfood.heal.ui.destination.podcast

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.junkfood.heal.ui.component.BottomDrawer

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun FilterDrawer() {

    BottomDrawer(
        drawerState = androidx.compose.material.rememberModalBottomSheetState(ModalBottomSheetValue.Expanded),
        sheetContent = {
            SheetItem("仅显示未收听的单集")
            SheetItem("仅显示已收听的单集")
            SheetItem("按时间正序排列")
            SheetItem("按时间倒序排列")
        })
}

@Composable
fun SheetItem(text: String) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { }) {
        Text(text,modifier = Modifier.padding(horizontal = 40.dp, vertical = 12.dp))
    }
}