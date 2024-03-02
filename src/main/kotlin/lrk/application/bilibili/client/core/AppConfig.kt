package lrk.application.bilibili.client.core

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import lrk.application.bilibili.client.getPlatformCharset
import lrk.application.bilibili.client.getPlatformDataDir
import java.io.File
import java.io.FileOutputStream

@Suppress("unused")
object AppConfig {
    const val DEBUG = true
    const val APP_NAME = "BiliBiliClient"
    const val APP_KEY = "1d8b6e7d45233436"
    const val APP_SEC = "560c52ccd288fed045859ed18bffd973"
    const val APP_USER_AGENT = "Mozilla/5.0 (X11; Linux x86_64; rv:122.0) Gecko/20100101 Firefox/122.0"

    val APP_VIDEO_CACHE_FILE = File(getPlatformDataDir().path + File.separator + "Cache.mp4")
    val APP_VIDEO_MRL: String = APP_VIDEO_CACHE_FILE.absolutePath

    // request a login qrcode
    const val API_QRCODE_LOGIN_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/generate"

    // get the qrcode scan state
    const val API_QRCODE_CHECK_URL = "https://passport.bilibili.com/x/passport-login/web/qrcode/poll"

    // gat user information
    const val API_USER_INFORMATION_URL = "https://api.bilibili.com/x/web-interface/nav"

    // check if the cookie should to be refreshed
    const val API_COOKIE_REFRESH_CHECK_URL = "https://passport.bilibili.com/x/passport-login/web/cookie/info"

    // get some recommend videos
    const val API_GET_RECOMMEND_VIDEOS_URL = "https://api.bilibili.com/x/web-interface/index/top/rcmd"

    // get the address of video
    const val API_GET_VIDEO_ADDRESS_URL = "https://api.bilibili.com/x/player/wbi/playurl"

    var cookie: MutableMap<String, ArrayList<LinkedTreeMap<String, String>>> = mutableMapOf()

    val cookieFile: File = File(getPlatformDataDir().path + File.separator + "cookies")

    fun saveCookie() {
        if (!cookieFile.exists()) cookieFile.createNewFile()
        FileOutputStream(cookieFile).use {
            it.write(Gson().toJson(cookie).toByteArray(getPlatformCharset()))
        }
    }

}