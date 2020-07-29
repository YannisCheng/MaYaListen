package com.yannis.baselib.utils.status_bar

import android.os.Build

import android.text.TextUtils
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*


/**
 * OSUtils Rom类型判断的工具类
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/29
 */

object OSUtils {
    const val ROM_MIUI = "MIUI"
    const val ROM_EMUI = "EMUI"
    const val ROM_FLYME = "FLYME"
    const val ROM_OPPO = "OPPO"
    const val ROM_SMARTISAN = "SMARTISAN"
    const val ROM_VIVO = "VIVO"
    const val ROM_QIKU = "QIKU"
    private const val KEY_VERSION_MIUI = "ro.miui.ui.version.name"
    private const val KEY_VERSION_EMUI = "ro.build.version.emui"
    private const val KEY_VERSION_OPPO = "ro.build.version.opporom"
    private const val KEY_VERSION_SMARTISAN = "ro.smartisan.version"
    private const val KEY_VERSION_VIVO = "ro.vivo.os.version"
    private var sName: String? = null
    private var sVersion: String? = null
    fun isEmui(): Boolean {
        return check(ROM_EMUI)
    }

    fun isMiui(): Boolean {
        return check(ROM_MIUI)
    }

    fun isVivo(): Boolean {
        return check(ROM_VIVO)
    }

    fun isOppo(): Boolean {
        return check(ROM_OPPO)
    }

    fun isFlyme(): Boolean {
        return check(ROM_FLYME)
    }

    fun is360(): Boolean {
        return check(ROM_QIKU) || check("360")
    }

    fun isSmartisan(): Boolean {
        return check(ROM_SMARTISAN)
    }

    fun getName(): String? {
        if (sName == null) {
            check("")
        }
        return sName
    }

    fun getVersion(): String? {
        if (sVersion == null) {
            check("")
        }
        return sVersion
    }

    fun check(rom: String): Boolean {
        if (sName != null) {
            return sName == rom
        }
        if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_MIUI).also { sVersion = it }
            )
        ) {
            sName = ROM_MIUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_EMUI).also { sVersion = it }
            )
        ) {
            sName = ROM_EMUI
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_OPPO).also { sVersion = it }
            )
        ) {
            sName = ROM_OPPO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_VIVO).also { sVersion = it }
            )
        ) {
            sName = ROM_VIVO
        } else if (!TextUtils.isEmpty(
                getProp(KEY_VERSION_SMARTISAN).also { sVersion = it }
            )
        ) {
            sName = ROM_SMARTISAN
        } else {
            sVersion = Build.DISPLAY
            val tempVersion = sVersion
            if (tempVersion != null) {
                if (tempVersion.toUpperCase(Locale.ROOT).contains(ROM_FLYME)) {
                    sName = ROM_FLYME
                } else {
                    sVersion = Build.UNKNOWN
                    sName = Build.MANUFACTURER.toUpperCase(Locale.ROOT)
                }
            }
        }
        return sName == rom
    }

    fun getProp(name: String): String? {
        var line: String? = null
        var input: BufferedReader? = null
        try {
            val p = Runtime.getRuntime().exec("getprop $name")
            input = BufferedReader(InputStreamReader(p.inputStream), 1024)
            line = input.readLine()
            input.close()
        } catch (ex: IOException) {
            return null
        } finally {
            if (input != null) {
                try {
                    input.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return line
    }
}

