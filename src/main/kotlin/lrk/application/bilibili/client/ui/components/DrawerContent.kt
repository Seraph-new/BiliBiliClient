package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lrk.application.bilibili.client.Platform.Companion.platformScaled
import lrk.application.bilibili.client.core.log.Log

@Composable
fun HomePageDrawerContent() {
    val userInfoColumnWidth = 400.dp
    MaterialTheme {
        BoxWithConstraints {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                // User Info Page
                Surface(modifier = Modifier.fillMaxHeight().width(userInfoColumnWidth.platformScaled())) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        UserInfoBlock()
                    }
                }
                // Application Log Page
                Surface(
                    modifier = Modifier.fillMaxHeight().width(this@BoxWithConstraints.maxWidth - userInfoColumnWidth),
                ) {
                    Column(
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Button(onClick = {
                            Log.clear()
                        }){
                            Text("Clean Log Queue")
                        }
                        LogInfoBlock()
                    }
                }
            }
        }
    }
}