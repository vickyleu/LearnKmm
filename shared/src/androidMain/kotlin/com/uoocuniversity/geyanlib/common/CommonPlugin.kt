package com.uoocuniversity.geyanlib.common

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

final class CommonPlugin<T : kMethodChannel> constructor(private val channelName: String) :Any(),
    FlutterPlugin, ActivityAware, MethodChannel.MethodCallHandler {
    private lateinit var realChannel: MethodChannel
    internal lateinit var _methodChannel:T

    private val methodChannel: T
        get() = _methodChannel

    final override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        val rltDartPointer = result
        methodChannel.onMethodCall(
            call = CommonMethodCall(
                method = call.method,
                arguments = call.arguments,
                platformDependencies = PlatformDependencies(activityPluginBinding?.activity, null)
            ),
            result = object : CommonMethodChannel.Result {
                override fun success(result: Any?) {
                    rltDartPointer.success(result)
                }

                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                    rltDartPointer.error(errorCode, errorMessage, errorDetails)
                }

                override fun notImplemented() {
                    rltDartPointer.notImplemented()
                }
            }
        )

    }

    private var activityPluginBinding: ActivityPluginBinding? = null
    private var engineBinding: FlutterPlugin.FlutterPluginBinding? = null
    final override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    final override fun onDetachedFromActivity() {
        activityPluginBinding = null
        if (this::realChannel.isInitialized) {
            this.realChannel.setMethodCallHandler(null)
        }
    }

    final override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    final override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    final override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        engineBinding = binding
        if (!this::realChannel.isInitialized) {
            this.realChannel = MethodChannel(binding.binaryMessenger, channelName)
        }
        this.realChannel.setMethodCallHandler(this)
    }

    final override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        engineBinding = null
        if (this::realChannel.isInitialized) {
            this.realChannel.setMethodCallHandler(null)
        }
    }



}


