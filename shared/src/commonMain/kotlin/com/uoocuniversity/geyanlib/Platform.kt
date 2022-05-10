package com.uoocuniversity.geyanlib
import com.uoocuniversity.geyanlib.common.PlatformDependencies

expect class Platform{
    actual fun preload()
}

expect class PlatformFactory(platformDeps: PlatformDependencies) {
    actual fun createPlatform(): Platform
}