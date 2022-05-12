package com.uoocuniversity.geyanlib.common

expect class CommonPlugin<T : kMethodChannel>(channelName: String) {
     actual val methodChannel: T
}

