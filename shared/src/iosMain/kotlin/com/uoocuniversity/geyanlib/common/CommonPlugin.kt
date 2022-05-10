package com.uoocuniversity.geyanlib.common

import cocoapods.Flutter.*
import platform.Foundation.NSCocoaErrorDomain
import platform.Foundation.NSError
import platform.darwin.NSObject

actual abstract class CommonPlugin<T : kMethodChannel> actual constructor(val channelName:String) : NSObject(),FlutterPluginProtocolMeta,
    FlutterPluginProtocol{
    internal actual var methodChannel: T
        get() = TODO("Not yet implemented")
        set(value) {}

    actual abstract fun createMethodChannel(): T

    override fun registerWithRegistrar(registrar: NSObject) {
        val engine = registrar as FlutterPluginRegistrarProtocol
        engine.addMethodCallDelegate(
            delegate = this,
            channel = FlutterMethodChannel(channelName,binaryMessenger = registrar.messenger(), codec = FlutterStandardMethodCodec())
        )
    }

    override fun handleMethodCall(call: FlutterMethodCall, result: FlutterResult) {
        super.handleMethodCall(call, result)
        methodChannel.onMethodCall(
            call = CommonMethodCall(
                method = call.method,
                arguments = call.arguments,
                platformDependencies = PlatformDependencies(null, call.arguments)
            ),
            result = object : CommonMethodChannel.Result {
                override fun success(successResult: Any?) {
                    result?.invoke(successResult)
                }

                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                    @Suppress("UNCHECKED_CAST")
                    result?.invoke(
                        NSError.errorWithDomain(
                            domain = NSCocoaErrorDomain,
                            code = errorCode.toLong(),
                            userInfo = hashMapOf(
                                "errorMessage" to errorMessage,
                                "errorDetails" to errorDetails
                            ) as Map<Any?, *>
                        )
                    )
                }

                override fun notImplemented() {
                    result?.invoke(FlutterMethodNotImplemented)
                }
            }
        )
    }
}