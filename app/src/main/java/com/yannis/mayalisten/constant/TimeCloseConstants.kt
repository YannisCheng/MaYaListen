package com.yannis.mayalisten.constant

import com.yannis.mayalisten.bean.TimeCloseBean

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class TimeCloseConstants {

    companion object {
        @JvmStatic
        var list = listOf<TimeCloseBean>(
            TimeCloseBean(false, "不开启"),
            TimeCloseBean(false, "播完当前声音"),
            TimeCloseBean(false, "播完2集声音"),
            TimeCloseBean(false, "播完3集声音"),
            TimeCloseBean(false, "10分钟"),
            TimeCloseBean(false, "20分钟"),
            TimeCloseBean(false, "30分钟"),
            TimeCloseBean(false, "60分钟"),
            TimeCloseBean(false, "90分钟"),
            TimeCloseBean(false, "自定义)")
        )
    }

}