package com.yannis.mayalisten.net

import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * MaYaApi 喜马拉雅音频相关
 *
 * @author  wenjia.Cheng  cwj1714@163.com
 * @date    2020/6/7
 */
interface MaYaApi {

    /**
     * 首页顶部tab数据获取
     */
    @GET("discovery-ranking-web/v3/ranking/AggregateRankFirstPage/1590971595389")
    fun getAggregateRankFirstPage(): Observable<BaseResultBean<ArrayList<AggregateRankFirstPageBean>>>

    /**
     * 首页单个tab-> 左侧所有list
     */
    @GET("discovery-ranking-web/v3/ranking/AggregateRankListTabs/1590971595575")
    fun getAggregateRankListTabs(@Query("rankingListId") rankingListId: Int): Observable<BaseResultBean<ArrayList<AggregateRankListTabsBean>>>

    /**
     * 首页单个tab -> 左侧单个list -> 所有专辑
     */
    @GET("discovery-ranking-web/v3/ranking/concreteRankList/1590971595956")
    fun getConcreteRankList(
        @Query("categoryId") categoryId: Int,
        @Query("clusterType") clusterType: Int,
        @Query("pageId") pageId: Int,
        @Query("pageSize") pageSize: Int,
        @Query("rankingListId") rankingListId: Int
    ): Observable<BaseResultBean<ConcreteRankListBean>>

    /**
     * 单张专辑所有内容
     */
    @GET("mobile-album/album/page/ts-1590971941673?ac=WIFI&device=iPhone&isQueryInvitationBrand=true&isVideoAsc=true&pageSize=20&source=0")
    fun getSingleAlbumContent(
        @Query("albumId") albumId: Int,
        @Query("isAsc") isAsc: Boolean,
        @Query("trackId") trackId: Int
    ): Observable<BaseResultBean<SingleAlbumContentBean>>

    /**
     * 对专辑自身的评价
     */
    @GET("album-comment-mobile/album/comment/list/query/1591776846264?order=content-score-desc&pageId=1&pageSize=20")
    fun getAlbumEvaluations(
        @Query("albumId") albumId: Int
    ): Observable<BaseResultBean<AlbumEvaluationsBean>>

    /**
     * 专辑-单条音频播放-声音内容
     */
    @GET("mobile/track/v2/baseInfo/1591777317565?device=android")
    fun getAlbumPlayEntry(
        @Query("trackId") trackId: Int
    ): Observable<AlbumPlayVoiceBean>

    /**
     * 专辑-单条音频播放-评价
     */
    @GET("comment-mobile/v1/track/comment/1591777645643?hotPageId=1&hotPageSize=30&imageViewSize=960&order=0&pageId=2&pageSize=30")
    fun getAlbumPlayEntryEvaluations(
        @Query("trackId") trackId: Int
    ): Observable<AlbumPlayEntryEvaluationBean>
}