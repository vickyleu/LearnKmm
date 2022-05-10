package com.uoocuniversity.geyanlib.common

expect class CommonPlugin<T : kMethodChannel>(channelName: String) {
    internal var methodChannel: T
    actual fun createMethodChannel(): T
}

