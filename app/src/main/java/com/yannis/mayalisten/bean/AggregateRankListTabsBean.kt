package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 首页单个tab-> 左侧所有list
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/7
 */
data class AggregateRankListTabsBean(
    val activityPicClicked: Any,
    val activityPicUnClicked: Any,
    val categoryId: Int,
    val contentType: String,
    val displayName: String,
    val name: String,
    val rankClusterId: Int,
    val rankingListId: Int,
    val rankingRule: String,
    val sortRuleDesc: String,
    val updateAtDesc: String,
    var isChecked: Boolean = false
) : Serializable {
    override fun toString(): String {
        return "AggregateRankListTabsBean(activityPicClicked=$activityPicClicked, activityPicUnClicked=$activityPicUnClicked, categoryId=$categoryId, contentType='$contentType', displayName='$displayName', name='$name', rankClusterId=$rankClusterId, rankingListId=$rankingListId, rankingRule='$rankingRule', sortRuleDesc='$sortRuleDesc', updateAtDesc='$updateAtDesc')"
    }
}