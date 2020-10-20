package com.yannis.mayalisten.bean

import java.io.Serializable

/**
 * 专辑-单条音频播放-弹幕
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/10
 */
data class AlbumPlayEntryBarrage(
    val `data`: Data,
    val msg: String,
    val ret: Int
) : Serializable {
    override fun toString(): String {
        return "AlbumPlayEntryBarrage(`data`=$`data`, msg='$msg', ret=$ret)"
    }
}

data class Data(
    val comments: List<CommentBean>,
    val second: Int
) : Serializable {
    override fun toString(): String {
        return "Data(comments=$comments, second=$second)"
    }
}

data class CommentBean(
    val bulletColor: Int,
    val content: String,
    val createdAt: Long,
    val id: Int,
    val isVip: Boolean,
    val liked: Boolean,
    val likes: Int,
    val parentId: Int,
    val shareCount: Int,
    val startTime: Int,
    val status: String,
    val trackId: Int,
    val trackUid: Int,
    val track_id: Int,
    val type: Int,
    val uid: Int,
    val vipExpireTime: Int
) : Serializable {
    override fun toString(): String {
        return "CommentBean(bulletColor=$bulletColor, content='$content', createdAt=$createdAt, id=$id, isVip=$isVip, liked=$liked, likes=$likes, parentId=$parentId, shareCount=$shareCount, startTime=$startTime, status='$status', trackId=$trackId, trackUid=$trackUid, track_id=$track_id, type=$type, uid=$uid, vipExpireTime=$vipExpireTime)"
    }
}