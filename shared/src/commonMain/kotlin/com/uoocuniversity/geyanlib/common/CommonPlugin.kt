package com.uoocuniversity.geyanlib.common

expect abstract class CommonPlugin<T: kMethodChannel>(channelName:String){
    internal var methodChannel:T
    abstract fun createMethodChannel():T
}