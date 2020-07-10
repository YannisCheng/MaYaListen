package com.yannis.mayalisten.tinker

import com.tencent.tinker.loader.app.TinkerApplication
import com.tencent.tinker.loader.shareutil.ShareConstants

/**
 * tinker热修复自定义application
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/7/10
 */
class SampleApplication(
    tinkerFlags: Int = ShareConstants.TINKER_ENABLE_ALL,
    delegateClassName: String? = "com.yannis.mayalisten.tinker.SampleApplicationLike",
    loaderClassName: String? = "com.tencent.tinker.loader.TinkerLoader",
    tinkerLoadVerifyFlag: Boolean = false
) : TinkerApplication(tinkerFlags, delegateClassName, loaderClassName, tinkerLoadVerifyFlag) {
}