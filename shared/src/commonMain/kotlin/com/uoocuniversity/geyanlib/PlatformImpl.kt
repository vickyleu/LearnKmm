package com.uoocuniversity.geyanlib

import com.uoocuniversity.geyanlib.common.CommonMethodCall
import com.uoocuniversity.geyanlib.common.CommonMethodChannel
import com.uoocuniversity.geyanlib.common.PlatformDependencies
import com.uoocuniversity.geyanlib.common.kMethodChannel

class PlatformImpl: kMethodChannel {
    lateinit var platform: Platform

    override fun onMethodCall(call: CommonMethodCall, result: CommonMethodChannel.Result) {

    }

    suspend fun initGeyanSdk(deps: PlatformDependencies): String {
        platform=PlatformFactory(deps).createPlatform()
        return ""
    }


}