package com.yannis.baselib.net

class RequestThrowable(var errorCode: Int, var errorMsg: String) : Throwable() {
}