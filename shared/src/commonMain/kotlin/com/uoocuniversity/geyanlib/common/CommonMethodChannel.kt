package com.uoocuniversity.geyanlib.common

class CommonMethodChannel {
    interface Result {
        fun success(result: Any?)
        fun error(errorCode: String, errorMessage: String?, errorDetails: Any?)
        fun notImplemented()
    }
}