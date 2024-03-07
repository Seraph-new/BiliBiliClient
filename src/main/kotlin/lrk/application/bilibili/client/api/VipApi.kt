package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.getCookie
import lrk.application.bilibili.client.core.log.logE
import lrk.application.bilibili.client.core.log.logI
import lrk.application.bilibili.client.core.makeGetRequestWithCookie
import lrk.application.bilibili.client.core.makeGetURL

fun BilibiliApi.vipSignIn(): Boolean {
    val request =
        makeGetRequestWithCookie(makeGetURL(API_VIP_SIGN, "csrf" to getCookie("passport.bilibili.com", "bili_jct")))
    println(request.url)
    val response = Client.getClient().newCall(request).execute()
    val result = when (response.isSuccessful) {
        true -> {
            val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
            val code = responseJsonObject.get("code").asInt
            println(code)
            if (code == 0) {
                true
            } else {
                logI(responseJsonObject.get("message").asString)
                false
            }
        }

        false -> false
    }
    response.close()
    return result
}