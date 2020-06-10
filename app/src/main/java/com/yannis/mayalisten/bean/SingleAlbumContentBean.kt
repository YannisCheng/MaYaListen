package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 单张专辑所有内容
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/8
 */
data class SingleAlbumContentBean(
    val album: Album,
    val tracks: Tracks,
    val user: User
) : Serializable

data class Album(
    val ageLevel: Int,
    val albumId: Int,
    val albumTimeLimited: Boolean,
    val authorizedExpireTime: Int,
    val authorizedTypeId: Int,
    val avatarPath: String,
    val buyNotes: String,
    val canInviteListen: Boolean,
    val canShareAndStealListen: Boolean,
    val categoryId: Int,
    val categoryName: String,
    val contractStatus: Int,
    val coverLarge: String,
    val coverMiddle: String,
    val coverSmall: String,
    val coverWebLarge: String,
    val createdAt: Long,
    val customSubTitle: String,
    val customTitle: String,
    val detailCoverPath: String,
    val financialStatus: Int,
    val followers: Int,
    val freeToPaidStatus: Int,
    val freeToPaidTime: Int,
    val hasRecs: Boolean,
    val intro: String,
    val introRich: String,
    val isAuthorized: Boolean,
    val isDefault: Boolean,
    val isDraft: Boolean,
    val isFinished: Int,
    val isInBlacklist: Boolean,
    val isNoCopyright: Boolean,
    val isPaid: Boolean,
    val isPublic: Boolean,
    val isRecordDesc: Boolean,
    val isVerified: Boolean,
    val isVipFree: Boolean,
    val is_default: Boolean,
    val lastUptrackAt: Long,
    val lastUptrackCoverPath: String,
    val lastUptrackId: Int,
    val lastUptrackTitle: String,
    val nickname: String,
    val offlineType: Int,
    val originalStatus: Int,
    val other_content: String,
    val other_title: String,
    val outline: String,
    val personalDescription: String,
    val playTimes: Int,
    val playTrackId: Int,
    val preferredType: Int,
    val recommendReason: String,
    val salePoint: String,
    val salePointPopup: String,
    val saleScope: Int,
    val serialState: Int,
    val serializeStatus: Int,
    val shareSupportType: Int,
    val shares: Int,
    val shortIntro: String,
    val shortIntroRich: String,
    val status: Int,
    val subscribeCount: Int,
    val tags: String,
    val title: String,
    val tracks: Int,
    val tracksIsAllPurchased: Boolean,
    val type: Int,
    val uid: Int,
    val unReadAlbumCommentCount: Int,
    val updatedAt: Long,
    val vipFirstStatus: Int,
    val vipFreeType: Int

) : Serializable {
    override fun toString(): String {
        return "Album(ageLevel=$ageLevel, albumId=$albumId, albumTimeLimited=$albumTimeLimited, authorizedExpireTime=$authorizedExpireTime, authorizedTypeId=$authorizedTypeId, avatarPath='$avatarPath', buyNotes='$buyNotes', canInviteListen=$canInviteListen, canShareAndStealListen=$canShareAndStealListen, categoryId=$categoryId, categoryName='$categoryName', contractStatus=$contractStatus, coverLarge='$coverLarge', coverMiddle='$coverMiddle', coverSmall='$coverSmall', coverWebLarge='$coverWebLarge', createdAt=$createdAt, customSubTitle='$customSubTitle', customTitle='$customTitle', detailCoverPath='$detailCoverPath', financialStatus=$financialStatus, followers=$followers, freeToPaidStatus=$freeToPaidStatus, freeToPaidTime=$freeToPaidTime, hasRecs=$hasRecs, intro='$intro', introRich='$introRich', isAuthorized=$isAuthorized, isDefault=$isDefault, isDraft=$isDraft, isFinished=$isFinished, isInBlacklist=$isInBlacklist, isNoCopyright=$isNoCopyright, isPaid=$isPaid, isPublic=$isPublic, isRecordDesc=$isRecordDesc, isVerified=$isVerified, isVipFree=$isVipFree, is_default=$is_default, lastUptrackAt=$lastUptrackAt, lastUptrackCoverPath='$lastUptrackCoverPath', lastUptrackId=$lastUptrackId, lastUptrackTitle='$lastUptrackTitle', nickname='$nickname', offlineType=$offlineType, originalStatus=$originalStatus, other_content='$other_content', other_title='$other_title', outline='$outline', personalDescription='$personalDescription', playTimes=$playTimes, playTrackId=$playTrackId, preferredType=$preferredType, recommendReason='$recommendReason', salePoint='$salePoint', salePointPopup='$salePointPopup', saleScope=$saleScope, serialState=$serialState, serializeStatus=$serializeStatus, shareSupportType=$shareSupportType, shares=$shares, shortIntro='$shortIntro', shortIntroRich='$shortIntroRich', status=$status, subscribeCount=$subscribeCount, tags='$tags', title='$title', tracks=$tracks, tracksIsAllPurchased=$tracksIsAllPurchased, type=$type, uid=$uid, unReadAlbumCommentCount=$unReadAlbumCommentCount, updatedAt=$updatedAt, vipFirstStatus=$vipFirstStatus, vipFreeType=$vipFreeType)"
    }
}

data class Tracks(
    val list: List<AlbumItemBean>
) : Serializable {
    override fun toString(): String {
        return "Tracks(list=$list)"
    }
}

data class User(
    val albums: Int,
    val anchorGrade: Int,
    val bizType: Int,
    val currentAnchorIsLiving: Boolean,
    val followers: Int,
    val followings: Int,
    val isVerified: Boolean,
    val liveRecordId: Int,
    val liveRoomId: Int,
    val liveStatus: Int,
    val nickname: String,
    val personDescribe: String,
    val personalSignature: String,
    val ptitle: String,
    val smallLogo: String,
    val subBizType: Int,
    val tracks: Int,
    val uid: Int,
    val vLogoType: Int,
    val verifyType: Int
) : Serializable {
    override fun toString(): String {
        return "User(albums=$albums, anchorGrade=$anchorGrade, bizType=$bizType, currentAnchorIsLiving=$currentAnchorIsLiving, followers=$followers, followings=$followings, isVerified=$isVerified, liveRecordId=$liveRecordId, liveRoomId=$liveRoomId, liveStatus=$liveStatus, nickname='$nickname', personDescribe='$personDescribe', personalSignature='$personalSignature', ptitle='$ptitle', smallLogo='$smallLogo', subBizType=$subBizType, tracks=$tracks, uid=$uid, vLogoType=$vLogoType, verifyType=$verifyType)"
    }
}

data class AlbumItemBean(
    val albumId: Int,
    val categoryId: Int,
    val commentId: String,
    val comments: Int,
    val coverLarge: String,
    val coverMiddle: String,
    val coverSmall: String,
    val createdAt: Long,
    val duration: Int,
    val id: Int,
    val isAuthorized: Boolean,
    val isDraft: Boolean,
    val isHoldCopyright: Boolean,
    val isPaid: Boolean,
    val isPublic: Boolean,
    val isRichAudio: Boolean,
    val isSample: Boolean,
    val isVideo: Boolean,
    val likes: Int,
    val nickname: String,
    val opType: Int,
    val orderNo: Int,
    val orderNum: Int,
    val paidType: Int,
    val playPathAacv164: String,
    val playPathAacv224: String,
    val playPathHq: String,
    val playUrl32: String,
    val playUrl64: String,
    val playtimes: Int,
    val processState: Int,
    val relatedId: Int,
    val sampleDuration: Int,
    val shares: Int,
    val smallLogo: String,
    val status: Int,
    val title: String,
    val trackId: Int,
    val trackRecordId: Int,
    val type: Int,
    val uid: Int,
    val userSource: Int,
    val vipFirstStatus: Int
) : Serializable {
    override fun toString(): String {
        return "AlbumItemBean(albumId=$albumId, categoryId=$categoryId, commentId='$commentId', comments=$comments, coverLarge='$coverLarge', coverMiddle='$coverMiddle', coverSmall='$coverSmall', createdAt=$createdAt, duration=$duration, id=$id, isAuthorized=$isAuthorized, isDraft=$isDraft, isHoldCopyright=$isHoldCopyright, isPaid=$isPaid, isPublic=$isPublic, isRichAudio=$isRichAudio, isSample=$isSample, isVideo=$isVideo, likes=$likes, nickname='$nickname', opType=$opType, orderNo=$orderNo, orderNum=$orderNum, paidType=$paidType, playPathAacv164='$playPathAacv164', playPathAacv224='$playPathAacv224', playPathHq='$playPathHq', playUrl32='$playUrl32', playUrl64='$playUrl64', playtimes=$playtimes, processState=$processState, relatedId=$relatedId, sampleDuration=$sampleDuration, shares=$shares, smallLogo='$smallLogo', status=$status, title='$title', trackId=$trackId, trackRecordId=$trackRecordId, type=$type, uid=$uid, userSource=$userSource, vipFirstStatus=$vipFirstStatus)"
    }
}