package com.uoocuniversity.geyanlib.common

actual abstract class CommonPluginImpl<T : kMethodChannel> actual constructor(channelName: String) {
    internal actual var innerPlugin: CommonPlugin<T> = CommonPlugin<T>(channelName)

    init {
        innerPlugin.createMethodChannel()
    }
    abstract fun createMethodChannel(): T
}