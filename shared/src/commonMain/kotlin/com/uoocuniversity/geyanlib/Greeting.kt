package com.uoocuniversity.geyanlib

class Greeting {
    lateinit var platform: Platform

    fun greeting(): String {
        platform=PlatformFactory(PlatformDependencies(null,null)).createPlatform()
        return ""
//        return "Hello, ${Platform().platform}!"
    }
}