package com.uoocuniversity.geyanlib

data class PlatformDependencies(
    val androidContext: Any?,
    val someIosOnlyDependency: Any?
)

expect class Platform() {
    actual fun preload()
}

expect class PlatformFactory(platformDeps: PlatformDependencies) {
    actual fun createPlatform(): Platform
}