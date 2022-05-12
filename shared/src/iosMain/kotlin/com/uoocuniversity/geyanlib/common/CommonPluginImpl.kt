package com.uoocuniversity.geyanlib.common

actual abstract class CommonPluginImpl<T : kMethodChannel> actual constructor(channelName: String) {
    internal actual val innerPlugin = CommonPlugin<T>(channelName)
    init {
        val channel:T = this.createMethodChannel()
        innerPlugin._methodChannel = channel
    }
    actual abstract fun createMethodChannel(): T
}