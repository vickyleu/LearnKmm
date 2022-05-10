package com.uoocuniversity.geyanlib.common

expect open class CommonPlugin<T: kMethodChannel>(channelName:String){
    internal var methodChannel:T
    actual fun createMethodChannel():T

}