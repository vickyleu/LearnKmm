package com.uoocuniversity.geyanlib.common

interface kMethodChannel{
    fun onMethodCall(call: CommonMethodCall, result: CommonMethodChannel.Result)
}