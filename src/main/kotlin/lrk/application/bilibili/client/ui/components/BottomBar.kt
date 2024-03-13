package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.Platform.Companion.platformScaled

@Composable
fun HomePageBottomBar(height: Dp = 50.dp) {
    BottomNavigation(modifier = Modifier.height(height.platformScaled())) {
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector =  Icons.Filled.Home, contentDescription = null)
                Text(text = "Home", fontSize = TextUnit(13F, TextUnitType.Sp).platformScaled())
            }
        })
    }
}