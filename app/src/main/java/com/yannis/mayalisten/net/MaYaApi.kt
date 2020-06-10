package com.yannis.mayalisten.net

import com.yannis.mayalisten.base.BaseResultBean
import com.yannis.mayalisten.bean.AggregateRankFirstPageBean
import com.yannis.mayalisten.bean.AggregateRankListTabsBean
import com.yannis.mayalisten.bean.ConcreteRankListBean
import com.yannis.mayalisten.bean.SingleAlbumContentBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
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
}