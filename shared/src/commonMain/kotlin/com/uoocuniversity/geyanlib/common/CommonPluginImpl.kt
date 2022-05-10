package com.uoocuniversity.geyanlib.common

expect abstract class CommonPluginImpl<T : kMethodChannel> constructor(channelName: String) {
    internal actual var innerPlugin: CommonPlugin<T>
}