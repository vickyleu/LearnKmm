package com.uoocuniversity.geyanlib.common

import cocoapods.Flutter.*
import kotlinx.cinterop.COpaquePointer
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.convert
import objcnames.classes.Protocol
import platform.Foundation.NSCocoaErrorDomain
import platform.Foundation.NSError
import platform.Foundation.NSNumber
import platform.darwin.NSInteger
import platform.darwin.NSObject
import platform.darwin.NSUInteger

final class CommonPlugin<T : kMethodChannel> constructor(private val channelName: String){
    internal lateinit var _methodChannel:T

    private val methodChannel: T
        get() = _methodChannel

    private val delegate = object :NSObject(), FlutterPluginProtocol{
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
                                code = errorCode.toLong().convert(),
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


    fun registerWithRegistrar(registrar: FlutterPluginRegistrarProtocol) {
        registrar.addMethodCallDelegate(
            delegate = delegate, channel =FlutterMethodChannel(
                channelName,
                binaryMessenger = registrar.messenger(),
                codec = FlutterStandardMethodCodec()
            ))
    }


}

