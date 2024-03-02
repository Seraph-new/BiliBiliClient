package lrk.application.bilibili.client.api

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import lrk.application.bilibili.client.core.AppConfig
import lrk.application.bilibili.client.core.AppState
import lrk.application.bilibili.client.core.Client
import lrk.application.bilibili.client.core.log.logD
import lrk.application.bilibili.client.core.makeGetRequestWithCookie
import lrk.application.bilibili.client.core.obj.NavigationUserInfoObj

object NavigationUserInfo {
    fun getNavigationUserInfo(): NavigationUserInfoObj? {
        logD("NavigationUserInfo::getNavigationUserInfo invoked")
        val request = makeGetRequestWithCookie(AppConfig.API_USER_INFORMATION_URL)
        val response = Client.getClient().newCall(request).execute()
        return when (response.isSuccessful) {
            true -> {
                val responseJsonObject: JsonObject = JsonParser.parseString(response.body.string()).asJsonObject
                if (responseJsonObject.get("code").asInt == 0) {
                    NavigationUserInfoObj.getInstance(responseJsonObject.get("data").asJsonObject)
                } else {
                    AppState.LoginState.shouldReLogin.value = true
                    null
                }
            }

            false -> null
        }
    }
}