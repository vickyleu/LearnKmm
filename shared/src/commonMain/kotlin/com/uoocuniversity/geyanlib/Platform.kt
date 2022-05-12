package com.uoocuniversity.geyanlib
import com.uoocuniversity.geyanlib.common.PlatformDependencies

expect class Platform{
    actual suspend fun preload()
}

expect class PlatformFactory(platformDeps: PlatformDependencies) {
    actual suspend fun createPlatform(): Platform
}