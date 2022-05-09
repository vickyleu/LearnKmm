package com.uoocuniversity.geyanlib

import android.content.Context
import com.g.gysdk.GYManager
import com.g.gysdk.GYResponse
import com.g.gysdk.GyCallBack

actual class Platform actual constructor() {
    lateinit var manager:GYManager
    actual fun preload(){

    }
}
actual class PlatformFactory actual constructor(val platformDeps: PlatformDependencies) {
    actual fun createPlatform():Platform{
        val context = platformDeps.androidContext as? Context
            ?: error("missing context in platform deps")

        return  Platform().also {
            it.manager = GYManager.getInstance()
            it.manager.init(context, object : GyCallBack {
                override fun onSuccess(response: GYResponse?) {
                }
                override fun onFailed(response: GYResponse?) {
                }
            })
        }
    }
}