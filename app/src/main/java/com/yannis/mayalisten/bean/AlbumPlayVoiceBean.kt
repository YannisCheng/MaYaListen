package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 专辑-单条音频播放-声音内容
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
data class AlbumPlayVoiceBean(
    val albumInfo: AlbumInfo,
    val msg: String,
    val ret: Int,
    val trackInfo: TrackInfo
) : Serializable {
    override fun toString(): String {
        return "AlbumPlayEntryBean(albumInfo=$albumInfo, msg='$msg', ret=$ret, trackInfo=$trackInfo)"
    }
}

data class AlbumInfo(
    val ageLevel: Int,
    val title: String,
    val vipFreeType: Int
) : Serializable {
    override fun toString(): String {
        return "AlbumInfo(ageLevel=$ageLevel, title='$title', vipFreeType=$vipFreeType)"
    }
}

data class TrackInfo(
    val albumId: Int,
    val albumTitle: String,
    val categoryId: Int,
    val categoryName: String,
    val comments: Int,
    val coverLarge: String,
    val coverMiddle: String,
    val coverSmall: String,
    val createdAt: Long,
    val downloadAacSize: Int,
    val downloadAacUrl: String,
    val downloadSize: Int,
    val downloadUrl: String,
    val duration: Int,
    val hqNeedVip: Boolean,
    val images: List<String>,
    val intro: String,
    val isAntiLeech: Boolean,
    val isAuthorized: Boolean,
    val isDraft: Boolean,
    val isFree: Boolean,
    val isPaid: Boolean,
    val isPublic: Boolean,
    val isRichAudio: Boolean,
    val isVideo: Boolean,
    val isVipFree: Boolean,
    val likes: Int,
    val nickname: String,
    val paidType: Int,
    val playHqSize: Int,
    val playPathAacv164: String,
    val playPathAacv164Size: Int,
    val playPathAacv224: String,
    val playPathAacv224Size: Int,
    val playPathHq: String,
    val playUrl32: String,
    val playUrl32Size: Int,
    val playUrl64: String,
    val playUrl64Size: Int,
    val playtimes: Int,
    val priceTypeEnum: Int,
    val priceTypeId: Int,
    val priceTypes: List<Any>,
    val processState: Int,
    val relatedId: Int,
    val ret: Int,
    val sampleDuration: Int,
    val shares: Int,
    val shortRichIntro: String,
    val status: Int,
    val title: String,
    val trackId: Int,
    val type: Int,
    val uid: Int,
    val userSource: Int,
    val videoCover: String,
    val vipFirstStatus: Int,
    val vipFreeType: Int
) : Serializable {
    override fun toString(): String {
        return "TrackInfo(albumId=$albumId, albumTitle='$albumTitle', categoryId=$categoryId, categoryName='$categoryName', comments=$comments, coverLarge='$coverLarge', coverMiddle='$coverMiddle', coverSmall='$coverSmall', createdAt=$createdAt, downloadAacSize=$downloadAacSize, downloadAacUrl='$downloadAacUrl', downloadSize=$downloadSize, downloadUrl='$downloadUrl', duration=$duration, hqNeedVip=$hqNeedVip, images=$images, intro='$intro', isAntiLeech=$isAntiLeech, isAuthorized=$isAuthorized, isDraft=$isDraft, isFree=$isFree, isPaid=$isPaid, isPublic=$isPublic, isRichAudio=$isRichAudio, isVideo=$isVideo, isVipFree=$isVipFree, likes=$likes, nickname='$nickname', paidType=$paidType, playHqSize=$playHqSize, playPathAacv164='$playPathAacv164', playPathAacv164Size=$playPathAacv164Size, playPathAacv224='$playPathAacv224', playPathAacv224Size=$playPathAacv224Size, playPathHq='$playPathHq', playUrl32='$playUrl32', playUrl32Size=$playUrl32Size, playUrl64='$playUrl64', playUrl64Size=$playUrl64Size, playtimes=$playtimes, priceTypeEnum=$priceTypeEnum, priceTypeId=$priceTypeId, priceTypes=$priceTypes, processState=$processState, relatedId=$relatedId, ret=$ret, sampleDuration=$sampleDuration, shares=$shares, shortRichIntro='$shortRichIntro', status=$status, title='$title', trackId=$trackId, type=$type, uid=$uid, userSource=$userSource, videoCover='$videoCover', vipFirstStatus=$vipFirstStatus, vipFreeType=$vipFreeType)"
    }
}