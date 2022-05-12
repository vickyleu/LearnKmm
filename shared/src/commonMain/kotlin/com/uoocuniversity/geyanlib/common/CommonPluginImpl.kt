package com.uoocuniversity.geyanlib.common

expect abstract class CommonPluginImpl<T : kMethodChannel> constructor(channelName: String) {
    internal actual val innerPlugin: CommonPlugin<T>

   actual abstract fun createMethodChannel(): T
}