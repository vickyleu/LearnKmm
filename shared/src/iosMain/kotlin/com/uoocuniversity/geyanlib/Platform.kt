package com.uoocuniversity.geyanlib
import cocoapods.GYSDK.GeYanSdk
import cocoapods.GYSDK.GyCallback
import com.uoocuniversity.geyanlib.common.PlatformDependencies
import platform.Foundation.NSError

actual class Platform {
    actual fun preload() {
        GeYanSdk.preGetToken {

        }
    }
}

actual class PlatformFactory actual constructor(val platformDeps: PlatformDependencies) {
    actual fun createPlatform(): Platform {
        val appId = platformDeps.someIosOnlyDependency as? String
            ?: error("missing appId in platform deps")

        return Platform().also {
            GeYanSdk.startWithAppId(appId, withCallback = object : GyCallback {
                override fun invoke(isSuccess: Boolean, error: NSError?, gyUid: String?) {

                }
            })
        }
    }
}
