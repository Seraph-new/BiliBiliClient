package lrk.application.bilibili.client.ui.components

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import lrk.application.bilibili.client.core.cleanVideoCache

@Composable
fun HomePageTopBar(drawerState: DrawerState) {
    val coroutineScope = rememberCoroutineScope()
    TopAppBar(
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
                Text("BiliBiliClient")
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
            Text(title)
        },
        actions = {

        },
    )
}