package com.uoocuniversity.geyanlib.common

data class CommonMethodCall(
    val method: String,
    val arguments: Any?,
    val platformDependencies: PlatformDependencies,
)