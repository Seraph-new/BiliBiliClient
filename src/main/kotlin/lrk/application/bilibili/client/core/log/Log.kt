package lrk.application.bilibili.client.core.log

import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.AppState

fun logI(message: String) {
    val logString = "[INFO]\t$message"
    AppState.LogState.logQueue.offer(logString)
    AppState.LogState.logEntries.value++
    println(logString)
}

fun logD(message: String) {
    if (AppConfig.DEBUG) {
        val logString = "[DEBUG]\t$message"
        AppState.LogState.logQueue.offer(logString)
        AppState.LogState.logEntries.value++
        println(logString)
    }
}

fun logE(message: String) {
    val logString = "[ERROR]\t$message"
    AppState.LogState.logQueue.offer(logString)
    AppState.LogState.logEntries.value++
    System.err.println(logString)
}
