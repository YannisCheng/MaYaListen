package com.yannis.imageuploadlib

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.qiniu.android.utils.StringMap
import java.lang.reflect.Type


/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/9/28
 */
object Json {
    fun encode(map: StringMap): String {
        return Gson().toJson(map.map())
    }

    fun encode(obj: Any?): String {
        return GsonBuilder().serializeNulls().create().toJson(obj)
    }

    fun <T> decode(json: String?, classOfT: Class<T>?): T {
        return Gson().fromJson(json, classOfT)
    }

    fun <T> decode(jsonElement: JsonElement?, clazz: Class<T>?): T {
        val gson = Gson()
        return gson.fromJson(jsonElement, clazz)
    }

    fun decode(json: String?): StringMap {
        // CHECKSTYLE:OFF
        val t: Type = object : TypeToken<Map<String?, Any?>?>() {}.type
        // CHECKSTYLE:ON
        val x = Gson().fromJson<Map<String, Any>>(json, t)
        return StringMap(x)
    }
}