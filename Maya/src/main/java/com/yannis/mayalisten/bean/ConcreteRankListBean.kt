package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 首页单个tab -> 左侧单个list -> 所有专辑
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
data class ConcreteRankListBean(
    val list: ArrayList<ItemBean>,
    val maxPageId: Int,
    val totalCount: Int,
    val pageId: Int,
    val pageSize: Int
) : Serializable {
    override fun toString(): String {
        return "ConcreteRankListBean(list=$list, maxPageId=$maxPageId, totalCount=$totalCount, pageId=$pageId, pageSize=$pageSize)"
    }
}

data class ItemBean(
    val activityLabel: Any,
    val ageLevel: Int,
    val albumCoverUrl290: String,
    val albumId: Int,
    val albumIntro: String,
    val albumScore: Any,
    val categoryId: Int,
    val coverLarge: String,
    val coverMiddle: String,
    val coverSmall: String,
    val customTitle: String,
    val editorTitle: String,
    val id: Int,
    val intro: String,
    val isDraft: Boolean,
    val isFinished: Int,
    val isPaid: Boolean,
    val isSample: Boolean,
    val isVipFree: Boolean,
    val lastUptrackAt: Long,
    val lastUptrackId: Int,
    val lastUptrackTitle: String,
    val materialType: String,
    val opType: Int,
    val originalCoverPath: String,
    val originalStatus: Int,
    val playsCounts: Int,
    val popularity: String,
    val positionChange: Int,
    val preferredType: Int,
    val provider: String,
    val recommendReason: String,
    val refundSupportType: Int,
    val serialState: Int,
    val status: Int,
    val subscribeCount: Int,
    val subscribeStatus: Any,
    val subscribesCounts: Any,
    val tags: String,
    val title: String,
    val trackId: Int,
    val tracks: Int,
    val type: Int,
    val uid: Int,
    val vipFreeType: Int
) : Serializable {
    override fun toString(): String {
        return "ItemBean(activityLabel=$activityLabel, ageLevel=$ageLevel, albumCoverUrl290='$albumCoverUrl290', albumId=$albumId, albumIntro='$albumIntro', albumScore=$albumScore, categoryId=$categoryId, coverLarge='$coverLarge', coverMiddle='$coverMiddle', coverSmall='$coverSmall', customTitle='$customTitle', editorTitle='$editorTitle', id=$id, intro='$intro', isDraft=$isDraft, isFinished=$isFinished, isPaid=$isPaid, isSample=$isSample, isVipFree=$isVipFree, lastUptrackAt=$lastUptrackAt, lastUptrackId=$lastUptrackId, lastUptrackTitle='$lastUptrackTitle', materialType='$materialType', opType=$opType, originalCoverPath='$originalCoverPath', originalStatus=$originalStatus, playsCounts=$playsCounts, popularity='$popularity', positionChange=$positionChange, preferredType=$preferredType, provider='$provider', recommendReason='$recommendReason', refundSupportType=$refundSupportType, serialState=$serialState, status=$status, subscribeCount=$subscribeCount, subscribeStatus=$subscribeStatus, subscribesCounts=$subscribesCounts, tags='$tags', title='$title', trackId=$trackId, tracks=$tracks, type=$type, uid=$uid, vipFreeType=$vipFreeType)"
    }
}