package com.uoocuniversity.geyanlib
import com.uoocuniversity.geyanlib.common.PlatformDependencies

expect class Platform{
    suspend fun preload()
}

expect class PlatformFactory(platformDeps: PlatformDependencies) {
    suspend fun createPlatform(): Platform
}