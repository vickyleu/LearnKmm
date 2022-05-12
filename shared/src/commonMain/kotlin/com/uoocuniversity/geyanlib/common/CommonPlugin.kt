package com.uoocuniversity.geyanlib.common

expect final class CommonPlugin<T : kMethodChannel>(channelName: String) {
//    internal actual var methodChannel: T
    internal  val methodChannel: T
}

