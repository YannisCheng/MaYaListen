package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/11
 */
class TimeCloseBean(var isChecked: Boolean, var itemDescribtion: String) : Serializable {
    override fun toString(): String {
        return "TimeCloseBean(isChecked=$isChecked, itemDescribtion='$itemDescribtion')"
    }
}