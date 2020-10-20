package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/17
 */
class IndexChooseBean(var indexInterval: String, var isCheckEd: Boolean) : Serializable {

    override fun toString(): String {
        return "IndexChooseBean(indexInterval='$indexInterval', isCheckEd=$isCheckEd)"
    }
}