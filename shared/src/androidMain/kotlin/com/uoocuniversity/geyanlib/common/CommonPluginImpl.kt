package com.uoocuniversity.geyanlib.common

actual abstract class CommonPluginImpl<T : kMethodChannel> actual constructor(private val channelName: String) {
    init {
        val channel:T = this.createMethodChannel()
        innerPlugin._methodChannel =channel
    }
    private val innerPlugin: CommonPlugin<T>
        get() = CommonPlugin<T>(channelName)

    actual abstract fun createMethodChannel(): T
}