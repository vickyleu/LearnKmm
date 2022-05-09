package com.uoocuniversity.geyanlib

class Greeting {
    fun greeting(): String {
        PlatformFactory(PlatformDependencies(null,null)).createPlatform()
        return ""
//        return "Hello, ${Platform().platform}!"
    }
}