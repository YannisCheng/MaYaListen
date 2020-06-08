package com.yannis.mayalisten.base

/**
 * 喜马拉雅接口返回值基础bean
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/7
 */
data class BaseResultBean<T>(
    val data: T,
    val msg: String,
    val ret: Int


) {
    override fun toString(): String {
        return "BaseResultBean(data=$data, msg='$msg', ret=$ret)"
    }
}