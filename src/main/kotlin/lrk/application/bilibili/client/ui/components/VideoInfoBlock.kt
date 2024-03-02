package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import lrk.application.bilibili.client.api.formatDuration
import lrk.application.bilibili.client.api.getBiliBiliNetworkPicture
import lrk.application.bilibili.client.api.getEmptyImageBitmap
import lrk.application.bilibili.client.core.APP_GLOBAL_NETWORK_THREAD_POOL
import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.log.logI
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.core.startVideoCachingProcess
import lrk.application.bilibili.client.ui.navigation.VideoPlayerScreen
import java.lang.Integer.max

@Composable
fun VideoInfoBlock(modifier: Modifier = Modifier, videoInfoObj: RecommendVideoInfoObj, id: Int) {
    val navigator = LocalNavigator.currentOrThrow
    var videoPic by remember {
        mutableStateOf(getEmptyImageBitmap(100, 100))
    }
    Surface(shape = RoundedCornerShape(15.dp), modifier = modifier.width(320.dp).height(100.dp).clickable {
        logI("VideoInfoBlock was clicked: ${videoInfoObj.title}, ${videoInfoObj.bvid}")
        startVideoCachingProcess(videoInfoObj.bvid, videoInfoObj.cid, 112)
        navigator.push(
            VideoPlayerScreen(
                AppConfig.APP_VIDEO_MRL,
                videoInfoObj.title
            )
        )
    }) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.Start,
        ) {
            Image(
                modifier = Modifier.fillMaxHeight().width(150.dp),
                bitmap = videoPic,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column(
                modifier = Modifier.fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                // title
                Text(
                    modifier = Modifier.fillMaxWidth().padding(end = 2.dp),
                    text = videoInfoObj.title,
                    fontSize = TextUnit(15F, TextUnitType.Sp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(5.dp))
                // video info
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = "UP: ${videoInfoObj.owner.name}",
                        fontSize = TextUnit(8F, TextUnitType.Sp),
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "时长: ${formatDuration(videoInfoObj.duration)}",
                        fontSize = TextUnit(10F, TextUnitType.Sp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = "${videoInfoObj.stat.view}播放 ${videoInfoObj.stat.like}点赞 ${videoInfoObj.stat.danmaku}弹幕",
                        fontSize = TextUnit(10F, TextUnitType.Sp)
                    )
                }
            }
        }
    }
    LaunchedEffect(Unit) {
        AppState.RecommendVideoPoolState.currentScrollIndex.value =
            max(id, AppState.RecommendVideoPoolState.currentScrollIndex.value)
        APP_GLOBAL_NETWORK_THREAD_POOL.execute {
            videoPic = getBiliBiliNetworkPicture(videoInfoObj.pic, 250, 200)!!
        }
    }
}