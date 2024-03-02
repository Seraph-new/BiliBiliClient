package lrk.application.bilibili.client.core

import com.google.gson.JsonParser
import lrk.application.bilibili.client.api.getVideoURL
import lrk.application.bilibili.client.core.log.logD
import java.io.BufferedInputStream
import java.io.FileOutputStream


fun startVideoCachingProcess(bvid: String, cid: Int, qn: Int) {
    cleanVideoCache()
    APP_GLOBAL_VIDEO_CACHING_PROCESS_THREAD_POOL.execute {
        AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD = Thread.currentThread()
        val sourceStream = BufferedInputStream(
            Client.getClient()
                .newCall(
                    makeGetRequestWithCookie(
                        getVideoURL(
                            JsonParser.parseString(
                                Client.getClient().newCall(
                                    makeGetRequestWithCookie(
                                        makeGetURLWithWbi(
                                            AppConfig.API_GET_VIDEO_ADDRESS_URL,
                                            "bvid" to bvid,
                                            "cid" to cid,
                                            "qn" to qn
                                        )
                                    )
                                ).execute().body.string()
                            ).asJsonObject.get("data").asJsonObject
                        )
                    )
                )
                .execute().body.byteStream()
        )

        AppState.VideoProcessState.CURRENT_SOURCE_STREAM = sourceStream
        AppState.VideoProcessState.SERVER_PREPARED = true
        if (!AppConfig.APP_VIDEO_CACHE_FILE.exists()) AppConfig.APP_VIDEO_CACHE_FILE.createNewFile()
        val targetStream = FileOutputStream(AppConfig.APP_VIDEO_CACHE_FILE)
        val buffer = ByteArray(1024)

        logD("Video Caching Process Started")

        try {
            while (!Thread.currentThread().isInterrupted) {
                val len = sourceStream.read(buffer) // 可以无视此行触发的异常
                if (len != -1 && !Thread.currentThread().isInterrupted) {
                    targetStream.write(buffer, 0, len)
                } else {
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        targetStream.close()
        sourceStream.close()

        logD("Video Caching Process End")
    }
}

fun cleanVideoCache(){
    AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD?.interrupt()
    AppState.VideoProcessState.CURRENT_VIDEO_CACHING_PROCESS_THREAD = null
    AppState.VideoProcessState.SERVER_PREPARED = false
    AppState.VideoProcessState.CURRENT_SOURCE_STREAM?.close()
    if (AppConfig.APP_VIDEO_CACHE_FILE.exists()) AppConfig.APP_VIDEO_CACHE_FILE.delete()
}