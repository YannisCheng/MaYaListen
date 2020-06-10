package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 专辑-单条音频播放-评价
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
data class AlbumPlayEntryEvaluationBean(
    val allowCommentType: Int,
    val allowCommentTypeDesc: String,
    val comment: Comment,
    val hotComment: HotComment,
    val msg: String,
    val ret: Int
) : Serializable {
    override fun toString(): String {
        return "AlbumPlayEntryEvaluationBean(allowCommentType=$allowCommentType, allowCommentTypeDesc='$allowCommentTypeDesc', comment=$comment, hotComment=$hotComment, msg='$msg', ret=$ret)"
    }
}

data class Comment(
    val list: List<EvaluationEntry>,
    val maxPageId: Int,
    val pageId: Int,
    val pageSize: Int,
    val totalCount: Int
) : Serializable {
    override fun toString(): String {
        return "Comment(list=$list, maxPageId=$maxPageId, pageId=$pageId, pageSize=$pageSize, totalCount=$totalCount)"
    }
}

data class HotComment(
    val list: List<X>,
    val maxPageId: Int,
    val pageId: Int,
    val pageSize: Int,
    val totalCount: Int
) : Serializable {
    override fun toString(): String {
        return "HotComment(list=$list, maxPageId=$maxPageId, pageId=$pageId, pageSize=$pageSize, totalCount=$totalCount)"
    }
}

data class EvaluationEntry(
    val bulletColor: Int,
    val content: String,
    val createdAt: Long,
    val giftQuantity: Int,
    val id: Int,
    val isTop: Boolean,
    val isVip: Boolean,
    val liked: Boolean,
    val likedUsers: List<Any>,
    val likes: Int,
    val nickname: String,
    val replyCount: Int,
    val shareCount: Int,
    val smallHeader: String,
    val startTime: Int,
    val status: String,
    val talentInfo: TalentInfo,
    val trackId: Int,
    val trackUid: Int,
    val type: Int,
    val uid: Int,
    val vipExpireTime: Int
) : Serializable {
    override fun toString(): String {
        return "EvaluationEntry(bulletColor=$bulletColor, content='$content', createdAt=$createdAt, giftQuantity=$giftQuantity, id=$id, isTop=$isTop, isVip=$isVip, liked=$liked, likedUsers=$likedUsers, likes=$likes, nickname='$nickname', replyCount=$replyCount, shareCount=$shareCount, smallHeader='$smallHeader', startTime=$startTime, status='$status', talentInfo=$talentInfo, trackId=$trackId, trackUid=$trackUid, type=$type, uid=$uid, vipExpireTime=$vipExpireTime)"
    }
}

class TalentInfo(
) : Serializable

data class X(
    val bulletColor: Int,
    val content: String,
    val createdAt: Long,
    val giftQuantity: Int,
    val id: Int,
    val isTop: Boolean,
    val isVip: Boolean,
    val liked: Boolean,
    val likedUsers: List<Any>,
    val likes: Int,
    val nickname: String,
    val replies: List<Reply>,
    val replyCount: Int,
    val shareCount: Int,
    val smallHeader: String,
    val startTime: Int,
    val status: String,
    val talentInfo: TalentInfoXX,
    val trackId: Int,
    val trackUid: Int,
    val type: Int,
    val uid: Int,
    val vipExpireTime: Int
) : Serializable {
    override fun toString(): String {
        return "X(bulletColor=$bulletColor, content='$content', createdAt=$createdAt, giftQuantity=$giftQuantity, id=$id, isTop=$isTop, isVip=$isVip, liked=$liked, likedUsers=$likedUsers, likes=$likes, nickname='$nickname', replies=$replies, replyCount=$replyCount, shareCount=$shareCount, smallHeader='$smallHeader', startTime=$startTime, status='$status', talentInfo=$talentInfo, trackId=$trackId, trackUid=$trackUid, type=$type, uid=$uid, vipExpireTime=$vipExpireTime)"
    }
}

data class Reply(
    val bulletColor: Int,
    val content: String,
    val createdAt: Long,
    val giftQuantity: Int,
    val id: Int,
    val isTop: Boolean,
    val isVip: Boolean,
    val liked: Boolean,
    val likedUsers: List<Any>,
    val likes: Int,
    val nickname: String,
    val pNickName: String,
    val parentId: Int,
    val parentUid: Int,
    val replyCount: Int,
    val shareCount: Int,
    val smallHeader: String,
    val startTime: Int,
    val status: String,
    val talentInfo: TalentInfoX,
    val trackId: Int,
    val trackUid: Int,
    val type: Int,
    val uid: Int,
    val vipExpireTime: Int
) : Serializable {
    override fun toString(): String {
        return "Reply(bulletColor=$bulletColor, content='$content', createdAt=$createdAt, giftQuantity=$giftQuantity, id=$id, isTop=$isTop, isVip=$isVip, liked=$liked, likedUsers=$likedUsers, likes=$likes, nickname='$nickname', pNickName='$pNickName', parentId=$parentId, parentUid=$parentUid, replyCount=$replyCount, shareCount=$shareCount, smallHeader='$smallHeader', startTime=$startTime, status='$status', talentInfo=$talentInfo, trackId=$trackId, trackUid=$trackUid, type=$type, uid=$uid, vipExpireTime=$vipExpireTime)"
    }
}

class TalentInfoXX(
) : Serializable

class TalentInfoX(
) : Serializable