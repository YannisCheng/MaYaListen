package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 对专辑自身的评价
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
data class AlbumEvaluationsBean(
    val comments: Comments,
    val isCommentsFolded: Boolean,
    val scoreDiagram: ScoreDiagram
) : Serializable {
    override fun toString(): String {
        return "AlbumEvaluationsBean(comments=$comments, isCommentsFolded=$isCommentsFolded, scoreDiagram=$scoreDiagram)"
    }
}

data class Comments(
    val list: List<Evaluation>,
    val maxPageId: Int,
    val pageId: Int,
    val pageSize: Int,
    val totalCount: Int
) : Serializable {
    override fun toString(): String {
        return "Comments(list=$list, maxPageId=$maxPageId, pageId=$pageId, pageSize=$pageSize, totalCount=$totalCount)"
    }
}

data class ScoreDiagram(
    val albumId: Int,
    val fiveStar: Double,
    val fourStar: Double,
    val isExited: Boolean,
    val oneStar: Double,
    val threeStar: Double,
    val twoStar: Double
) : Serializable {
    override fun toString(): String {
        return "ScoreDiagram(albumId=$albumId, fiveStar=$fiveStar, fourStar=$fourStar, isExited=$isExited, oneStar=$oneStar, threeStar=$threeStar, twoStar=$twoStar)"
    }
}

data class Evaluation(
    val albumCoverPath: String,
    val albumId: Int,
    val albumUid: Int,
    val auditStatus: Int,
    val commentId: Int,
    val content: String,
    val createdAt: Long,
    val isHighQuality: Boolean,
    val likes: Int,
    val newAlbumScore: Int,
    val nickname: String,
    val replyCount: Int,
    val smallHeader: String,
    val uid: Int,
    val updatedAt: Long,
    val vLogoType: Int
) : Serializable {
    override fun toString(): String {
        return "Evaluation(albumCoverPath='$albumCoverPath', albumId=$albumId, albumUid=$albumUid, auditStatus=$auditStatus, commentId=$commentId, content='$content', createdAt=$createdAt, isHighQuality=$isHighQuality, likes=$likes, newAlbumScore=$newAlbumScore, nickname='$nickname', replyCount=$replyCount, smallHeader='$smallHeader', uid=$uid, updatedAt=$updatedAt, vLogoType=$vLogoType)"
    }
}