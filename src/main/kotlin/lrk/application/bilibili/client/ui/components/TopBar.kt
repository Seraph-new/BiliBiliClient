package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.core.cleanVideoCache

@Composable
fun HomePageTopBar(drawerState: DrawerState, height: Dp = 50.dp) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
        modifier = Modifier.height(height.platformScaled()),
        navigationIcon = {
            IconButton(onClick = {
                if (drawerState.isClosed) {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
            }){
                Icon(imageVector = Icons.Default.Menu, contentDescription = null)
            }
        },
        title = {
            Text(text = "BiliBiliClient", fontSize = TextUnit(18F, TextUnitType.Sp).platformScaled())
        },
        actions = {

        },
    )
}

@Composable
fun VideoPlayerPageTopBar(navigator: Navigator?, title: String){
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                cleanVideoCache()
                navigator?.pop()
            }){
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
            }
        },
        title = {
            Text(text = title, fontSize = TextUnit(15F, TextUnitType.Sp).platformScaled())
        },
        actions = {

        },
    )
}