package lrk.application.bilibili.client.ui.components.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import lrk.application.bilibili.client.ui.theme.AppTheme
import kotlinx.coroutines.delay
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import lrk.application.bilibili.client.ui.components.VideoPlayerPageTopBar
import lrk.application.bilibili.client.ui.components.videoplayer.VideoPlayer
import lrk.application.bilibili.client.ui.components.videoplayer.rememberVideoPlayerState
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer

@Composable
fun VideoPlayerPage(url: String, videoInfoObj: RecommendVideoInfoObj) {
    val fullScreen = remember {
        mutableStateOf(false)
    }
    val navigator = LocalNavigator.current
    AppTheme {
        Scaffold(topBar = {
            if (!fullScreen.value) VideoPlayerPageTopBar(navigator = navigator, title = videoInfoObj.title)
        }) {
            BoxWithConstraints {
                Row(
                    modifier = Modifier.width(this.maxWidth).height(this.maxHeight)
                ) {
                    Column(
                        modifier = Modifier.height(this@BoxWithConstraints.maxHeight)
                    ) {
                        JetBrainsVideoPlayer(
                            url = url,
                            width = if (!fullScreen.value) Dp((this@BoxWithConstraints.maxWidth.value * 0.8).toFloat()) else Dp(
                                this@BoxWithConstraints.maxWidth.value
                            ),
                            height = if (!fullScreen.value) Dp((this@BoxWithConstraints.maxHeight.value * 0.75).toFloat()) else Dp(
                                this@BoxWithConstraints.maxHeight.value
                            ),
                            fullScreen
                        )
                        if (!fullScreen.value) {
                            Surface(
                                modifier = Modifier.width(Dp((this@BoxWithConstraints.maxWidth.value * 0.8).toFloat()))
                                    .height(Dp((this@BoxWithConstraints.maxHeight.value * 0.25).toFloat())),
                            ) {
                                // 视频简介
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("视频简介")
                                }
                            }
                        }
                    }
                    if (!fullScreen.value) {
                        // 视频评论区
                        Surface(
                            modifier = Modifier.height(this@BoxWithConstraints.maxHeight)
                                .width(Dp((this@BoxWithConstraints.maxWidth.value * 0.2).toFloat())),
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ){
                                Text("评论区")
                            }
                        }
                    }
                }
            }
        }
        LaunchedEffect(Unit) {
            while (!(AppState.MediaPlayerState.mediaPlayer as EmbeddedMediaPlayer).status().isPlayable) {
                (AppState.MediaPlayerState.mediaPlayer as EmbeddedMediaPlayer).media().start(url)
                delay(200)
            }
        }
    }
}

@Composable
fun JetBrainsVideoPlayer(url: String, width: Dp = 1200.dp, height: Dp = 500.dp, fullScreen: MutableState<Boolean>) {
    val state = rememberVideoPlayerState()
    /*
     * Could not use a [Box] to overlay the controls on top of the video.
     * See https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Swing_Integration
     * Related issues:
     * https://github.com/JetBrains/compose-multiplatform/issues/1521
     * https://github.com/JetBrains/compose-multiplatform/issues/2926
     */
    Surface(
        modifier = Modifier.width(width).height(height)
    ) {
        Column {
            VideoPlayer(
                url = url,
                state = state,
                onFinish = state::stopPlayback,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height - 44.dp)
            )
            Column(modifier = Modifier.fillMaxWidth().height(44.dp)) {
                Row(
                    modifier = Modifier.height(22.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 5.dp),
                        text = formatTime((state.progress.value.timeMillis / 1000).toInt()),
                        fontSize = TextUnit(12F, TextUnitType.Sp)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Speed {
                            state.speed = it ?: state.speed
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource("assets/player-resources/volume.svg"),
                            contentDescription = "Volume",
                            modifier = Modifier.size(14.dp)
                        )
                        // TODO: Make the slider change volume in logarithmic manner
                        //  See https://www.dr-lex.be/info-stuff/volumecontrols.html
                        //  and https://ux.stackexchange.com/q/79672/117386
                        //  and https://dcordero.me/posts/logarithmic_volume_control.html
                        Slider(
                            value = state.volume,
                            onValueChange = { state.volume = it },
                            modifier = Modifier.width(100.dp).height(14.dp)
                        )
                    }
                }
                BoxWithConstraints(modifier = Modifier.fillMaxWidth().height(22.dp).padding(bottom = 2.dp)) {
                    Row(
                        modifier = Modifier.padding(start = 3.dp, end = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(modifier = Modifier.size(16.dp),
                            onClick = {
                                state.toggleResume()
                            }) {
                            Icon(
                                painter = painterResource("assets/player-resources/${if (state.isResumed) "pause" else "play"}.svg"),
                                contentDescription = "Play/Pause",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Slider(
                            value = state.progress.value.fraction,
                            onValueChange = { state.seek = it },
                            modifier = Modifier.width(this@BoxWithConstraints.maxWidth - 45.dp)
                        )
                        IconButton(modifier = Modifier.size(16.dp),
                            onClick = {
                                state.toggleFullscreen()
                                fullScreen.value = !fullScreen.value
                            }) {
                            Icon(
                                painter = painterResource("assets/player-resources/${if (state.isFullscreen) "exit" else "enter"}-fullscreen.svg"),
                                contentDescription = "Toggle fullscreen",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * See [this Stack Overflow post](https://stackoverflow.com/a/67765652).
 */
@Composable
fun Speed(
    onChange: (Float?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        AnimatedVisibility(visible = expanded) {
            Spacer(modifier = Modifier.width(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(text = "0.5x", fontSize = TextUnit(13F, TextUnitType.Sp), modifier = Modifier.clickable {
                    onChange(0.5F)
                    expanded = !expanded
                })
                Text(text = "1.0x", fontSize = TextUnit(13F, TextUnitType.Sp), modifier = Modifier.clickable {
                    onChange(1.0F)
                    expanded = !expanded
                })
                Text(text = "1.5x", fontSize = TextUnit(13F, TextUnitType.Sp), modifier = Modifier.clickable {
                    onChange(1.5F)
                    expanded = !expanded
                })
                Text(text = "2.0x", fontSize = TextUnit(13F, TextUnitType.Sp), modifier = Modifier.clickable {
                    onChange(2.0F)
                    expanded = !expanded
                })
            }
        }
        Icon(
            painter = painterResource("assets/player-resources/speed.svg"),
            contentDescription = "Speed",
            modifier = Modifier.size(14.dp).clickable {
                expanded = !expanded
            }
        )
    }
}

fun formatTime(duration: Int): String {
    return when {
        duration < 60 -> {
            "${duration}s"
        }

        duration < 3600 -> {
            val minute = duration.div(60)
            val second = duration - 60 * minute
            "${minute}m${second}s"
        }

        else -> {
            val hour = duration.div(3600)
            val minute = (duration - hour * 3600).div(60)
            val second = duration - hour * 3600 - minute * 60
            "${hour}h${minute}m${second}s"
        }
    }
}