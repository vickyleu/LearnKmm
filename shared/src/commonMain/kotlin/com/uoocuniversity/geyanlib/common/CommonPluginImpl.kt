package com.uoocuniversity.geyanlib.common

expect abstract class CommonPluginImpl<T : kMethodChannel> constructor(channelName: String) {
    abstract fun createMethodChannel(): T
}