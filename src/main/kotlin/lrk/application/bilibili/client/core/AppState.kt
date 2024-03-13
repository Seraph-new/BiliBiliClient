package lrk.application.bilibili.client.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import lrk.application.bilibili.client.core.obj.RecommendVideoInfoObj
import java.io.InputStream
import java.util.*

object AppState {
    object LoginState {
        var qrcodeKey: String = ""
        var shouldReLogin: MutableState<Boolean> = mutableStateOf(false)
    }
    object LogState{
        val logQueue: LinkedList<String> = LinkedList()
        var logEntries: MutableState<Int> = mutableStateOf(0)
    }
    object RecommendVideoPoolState{
        val recommendVideoPool: LinkedHashSet<RecommendVideoInfoObj> = LinkedHashSet() // TODO: optimize data structure
        var recommendVideoPoolSize: MutableState<Int> = mutableStateOf(0)
        var currentScrollIndex: MutableState<Int> = mutableStateOf(0)
    }
    object VideoProcessState{
        var CURRENT_VIDEO_CACHING_PROCESS_THREAD: Thread? = null
        var SERVER_PREPARED: Boolean = false
        var CURRENT_SOURCE_STREAM: InputStream? = null
    }
    object MediaPlayerState{
        var mediaPlayer: Any? = null
    }
}