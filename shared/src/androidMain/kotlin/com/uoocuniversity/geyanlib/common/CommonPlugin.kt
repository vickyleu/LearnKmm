package com.uoocuniversity.geyanlib.common

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

actual abstract class CommonPlugin<T : kMethodChannel> actual constructor(val channelName:String) : FlutterPlugin, ActivityAware,
    MethodChannel.MethodCallHandler {

    private lateinit var realChannel : MethodChannel

    internal actual var methodChannel: T
        get() = TODO("Not yet implemented")
        set(value) {}

    actual abstract fun createMethodChannel(): T

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        methodChannel.onMethodCall(
            call = CommonMethodCall(
                method = call.method,
                arguments = call.arguments,
                platformDependencies = PlatformDependencies(activityPluginBinding?.activity, null)
            ),
            result = object : CommonMethodChannel.Result {
                override fun success(successResult: Any?) {
                    result.success(successResult)
                }

                override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
                    result.error(errorCode, errorMessage, errorDetails)
                }

                override fun notImplemented() {
                    result.notImplemented()
                }
            }
        )

    }

    private var activityPluginBinding: ActivityPluginBinding? = null
    private var engineBinding: FlutterPlugin.FlutterPluginBinding? = null
    override fun onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity()
    }

    override fun onDetachedFromActivity() {
        activityPluginBinding = null
        if(this::realChannel.isInitialized){
            this.realChannel.setMethodCallHandler(null)
        }
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        onAttachedToActivity(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activityPluginBinding = binding
    }

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        engineBinding = binding
        if(!this::realChannel.isInitialized){
            this.realChannel = MethodChannel(binding.binaryMessenger, channelName)
        }
        this.realChannel.setMethodCallHandler(this)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        engineBinding = null
        if(this::realChannel.isInitialized){
            this.realChannel.setMethodCallHandler(null)
        }
    }

}