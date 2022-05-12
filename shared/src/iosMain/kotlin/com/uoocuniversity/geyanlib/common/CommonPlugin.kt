package com.uoocuniversity.geyanlib.common

import cocoapods.Flutter.*
import kotlinx.cinterop.ObjCClass
import platform.Foundation.NSCocoaErrorDomain
import platform.Foundation.NSError
import platform.darwin.NSObject

actual class CommonPlugin<T : kMethodChannel> actual constructor(private val channelName: String) :NSObject(),
     FlutterPluginProtocolMeta, FlutterPluginProtocol {
    internal lateinit var _methodChannel:T
    actual val methodChannel: T = _methodChannel

    override fun registerWithRegistrar(registrar: NSObject) {
        val engine = registrar as FlutterPluginRegistrarProtocol
        engine.addMethodCallDelegate(
            delegate = this,
            channel = FlutterMethodChannel(
                channelName,
                binaryMessenger = registrar.messenger(),
                codec = FlutterStandardMethodCodec()
            )
        )
    }

    override fun handleMethodCall(call: FlutterMethodCall, result: FlutterResult) {
        val rltDartPointer = result
        methodChannel.onMethodCall(
            call = CommonMethodCall(
                method = call.method,
                arguments = call.arguments,
                platformDependencies = PlatformDependencies(null, call.arguments)
            ),
            result = object : CommonMethodChannel.Result {
                override fun success(result: Any?) {
                    rltDartPointer?.invoke(result)
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
                    rltDartPointer?.invoke(FlutterMethodNotImplemented)
                }
            }
        )
    }
}

