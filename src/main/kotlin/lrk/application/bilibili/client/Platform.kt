package lrk.application.bilibili.client

import lrk.application.bilibili.client.core.AppConfig
import java.io.File
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets


enum class Platform {
    Windows,
    Unix,
}

fun getPlatform(): Platform {
    return if (System.getProperty("os.name").lowercase().contains("windows")) {
        Platform.Windows
    } else {
        Platform.Unix
    }
}

fun getPlatformCharset(): Charset {
    return when (getPlatform()) {
        Platform.Windows -> Charset.forName("GBK")
        Platform.Unix -> StandardCharsets.UTF_8
    }
}

fun getPlatformDataDir(): File {
    return when (getPlatform()) {
        Platform.Windows -> {
            val file = File("${System.getenv("LOCALAPPDATA")}\\${AppConfig.APP_NAME}")
            if (file.isFile) file.delete()
            if (!file.exists()) file.mkdirs()
            file
        }

        Platform.Unix -> {
            val file = File("${System.getProperty("user.home")}/.config/${AppConfig.APP_NAME}")
            if (file.isFile) file.delete()
            if (!file.exists()) file.mkdirs()
            file
        }
    }
}

