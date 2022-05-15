package com.uoocuniversity.geyanlib.common

import cocoapods.Flutter.FlutterPluginRegistrarProtocol
import platform.darwin.NSObject

actual abstract class CommonPluginImpl<T : kMethodChannel> actual constructor(private val channelName: String) {
    private var innerPlugin: CommonPlugin<T>
    init {
        val channel: T = this.createMethodChannel()
        innerPlugin = CommonPlugin<T>(channelName)
        innerPlugin._methodChannel = channel
    }

    actual abstract fun createMethodChannel(): T

    fun registerWithRegistrar(registrar: FlutterPluginRegistrarProtocol){
        innerPlugin.registerWithRegistrar(registrar)
    }

}