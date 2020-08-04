package com.yannis.baselib.net

class RequestThrowable(private var errorCode:Int, private var errorMsg:String): Throwable()  {
}