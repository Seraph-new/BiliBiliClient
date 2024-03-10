package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.api.BilibiliApi
import lrk.application.bilibili.client.api.getRecommendVideo
import lrk.application.bilibili.client.core.APP_GLOBAL_EVENT_THREAD_POOL
import lrk.application.bilibili.client.core.APP_GLOBAL_NETWORK_THREAD_POOL
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.ui.components.BottomBar
import lrk.application.bilibili.client.ui.components.DrawerContent
import lrk.application.bilibili.client.ui.components.HomePageTopBar
import lrk.application.bilibili.client.ui.components.VideoInfoBlock

@Composable
fun HomePage() {
    val drawerState = DrawerState(DrawerValue.Closed)
    val scaffoldState = rememberScaffoldState(drawerState)
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            HomePageTopBar(drawerState = drawerState)
        },
        bottomBar = {
            BottomBar()
        },
        drawerContent = {
            DrawerContent()
        }
    ) {
        LazyVerticalGrid(
            state = rememberLazyGridState(),
            columns = GridCells.Adaptive(330.dp.platformScaled()),
            verticalArrangement = Arrangement.spacedBy(5.dp.platformScaled()),
            horizontalArrangement = Arrangement.spacedBy(5.dp.platformScaled()),
            modifier = Modifier.padding(top = 3.dp.platformScaled(), start = 3.dp.platformScaled(), end = 3.dp.platformScaled())
        ) {
            items(AppState.RecommendVideoPoolState.recommendVideoPoolSize.value, key = { it }) {
                VideoInfoBlock(
                    modifier = Modifier,
                    AppState.RecommendVideoPoolState.recommendVideoPool.toArray()[it] as RecommendVideoInfoObj, // 可能有性能问题
                    it
                )
            }
        }
    }
    LaunchedEffect(Unit) {
        APP_GLOBAL_EVENT_THREAD_POOL.execute {
            while (true) {
                if (AppState.RecommendVideoPoolState.recommendVideoPoolSize.value < AppState.RecommendVideoPoolState.currentScrollIndex.value + 20) {
                    APP_GLOBAL_NETWORK_THREAD_POOL.execute {
                        val recommendVideos = BilibiliApi.getRecommendVideo()
                        AppState.RecommendVideoPoolState.recommendVideoPool.addAll(recommendVideos)
                        AppState.RecommendVideoPoolState.recommendVideoPoolSize.value =
                            AppState.RecommendVideoPoolState.recommendVideoPool.size
                    }
                }
                Thread.sleep(500)
            }
        }
    }
}