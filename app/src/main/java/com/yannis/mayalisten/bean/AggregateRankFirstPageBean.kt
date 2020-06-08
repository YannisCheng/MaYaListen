package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 首页总排名tab数据bean
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/7
 */
data class AggregateRankFirstPageBean(
    val aggregateListConfig: AggregateListConfig,
    val aggregateListRule: AggregateListRule,
    val albumResults: List<Any>,
    val anchorResults: Any

) : Serializable {
    override fun toString(): String {
        return "AggregateRankFirstPageBean(aggregateListConfig=$aggregateListConfig, aggregateListRule=$aggregateListRule, albumResults=$albumResults, anchorResults=$anchorResults)"
    }
}

data class AggregateListConfig(
    val activityPicClicked: Any,
    val activityPicUnClicked: Any,
    val aggregateName: String,
    val anchorOrNot: Boolean,
    val bigCard: Boolean,
    val clusterType: Int,
    val rankClusterId: Int,
    val rankingListId: Int
) : Serializable {
    override fun toString(): String {
        return "AggregateListConfig(activityPicClicked=$activityPicClicked, activityPicUnClicked=$activityPicUnClicked, aggregateName='$aggregateName', anchorOrNot=$anchorOrNot, bigCard=$bigCard, clusterType=$clusterType, rankClusterId=$rankClusterId, rankingListId=$rankingListId)"
    }
}

data class AggregateListRule(
    val aggregateName: String,
    val calculateRule: String,
    val clusterType: Int,
    val updateTime: String
) : Serializable {
    override fun toString(): String {
        return "AggregateListRule(aggregateName='$aggregateName', calculateRule='$calculateRule', clusterType=$clusterType, updateTime='$updateTime')"
    }
}