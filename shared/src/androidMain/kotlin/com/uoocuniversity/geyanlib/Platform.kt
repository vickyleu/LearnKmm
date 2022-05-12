package com.uoocuniversity.geyanlib

import android.content.Context
import com.g.gysdk.GYManager
import com.g.gysdk.GYResponse
import com.g.gysdk.GyCallBack
import com.uoocuniversity.geyanlib.common.PlatformDependencies

actual class Platform constructor(val manager: GYManager) {
    actual suspend fun preload() {
        manager.ePreLogin(1000, object : GyCallBack {
            override fun onSuccess(response: GYResponse?) {
            }

            override fun onFailed(response: GYResponse?) {
            }
        })
    }
}

actual class PlatformFactory actual constructor(val platformDeps: PlatformDependencies) {
    actual suspend fun createPlatform(): Platform {
        val context = platformDeps.androidContext as? Context
            ?: error("missing context in platform deps")

        return Platform(GYManager.getInstance()).also {
            it.manager.init(context, object : GyCallBack {
                override fun onSuccess(response: GYResponse?) {
                }

                override fun onFailed(response: GYResponse?) {
                }
            })
        }
    }
}