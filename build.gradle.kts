import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "lrk.application.bilibili.client"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    testImplementation(kotlin("test"))
    implementation("com.google.zxing:core:3.5.3")
    implementation("com.google.zxing:javase:3.5.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    implementation("uk.co.caprica:vlcj:4.8.2")
    implementation("org.freedesktop.gstreamer:gst1-java-core:1.4.0")
    implementation("net.java.dev.jna:jna:5.14.0")
    implementation("net.java.dev.jna:jna-platform:5.14.0")
    implementation("cafe.adriel.voyager:voyager-navigator:1.1.0-alpha02")
}

compose.desktop {
    application {
        mainClass = "lrk.application.bilibili.client.Launcher"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Exe, TargetFormat.AppImage)
            packageName = "BiliBiliClient"
            packageVersion = "1.0.0"
        }
    }
}
