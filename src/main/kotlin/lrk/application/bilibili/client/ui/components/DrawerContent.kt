package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import lrk.application.bilibili.client.core.log.Log

@Composable
fun HomePageDrawerContent() {
    MaterialTheme {
        BoxWithConstraints {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                // Application Log Page
                Surface(
                    modifier = Modifier.fillMaxSize(),
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