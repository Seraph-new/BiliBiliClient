package lrk.application.bilibili.client.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun BottomBar() {
    BottomNavigation {
        BottomNavigationItem(selected = false, onClick = {}, icon = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector =  Icons.Filled.Home, contentDescription = null)
                Text("Home")
            }
        })
    }
}