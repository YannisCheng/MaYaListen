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

    @GET("discovery-ranking-web/v3/ranking/AggregateRankFirstPage/1590971595389")
    fun getAggregateRankFirstPage(): Observable<BaseResultBean<ArrayList<AggregateRankFirstPageBean>>>

    @GET("discovery-ranking-web/v3/ranking/AggregateRankListTabs/1590971595575")
    fun getAggregateRankListTabs(@Query("rankingListId") rankingListId: String): Observable<BaseResultBean<ArrayList<AggregateRankListTabsBean>>>

    /**
     * 选项卡具体专辑排名
     */
    @GET("discovery-ranking-web/v3/ranking/concreteRankList/1590971595956")
    fun getConcreteRankList(
        @Query("categoryId") categoryId: String,
        @Query("clusterType") clusterType: String,
        @Query("pageId") pageId: String,
        @Query("pageSize") pageSize: String,
        @Query("rankingListId") rankingListId: String
    ): Observable<BaseResultBean<ConcreteRankListBean>>

    /**
     * 专辑内容
     */
    @GET("mobile-album/album/page/ts-1590971941673?ac=WIFI&device=iPhone&isQueryInvitationBrand=true&isVideoAsc=true&pageSize=20&source=0")
    fun getSingleAlbumContent(
        @Query("albumId") albumId: String,
        @Query("isAsc") isAsc: String,
        @Query("trackId") trackId: String
    ): Observable<BaseResultBean<SingleAlbumContentBean>>
}