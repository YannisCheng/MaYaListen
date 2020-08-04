package com.yannis.mayalisten.view_mode

import com.yannis.baselib.base.BaseDepository
import com.yannis.baselib.base.BaseResultBean
import com.yannis.baselib.net.RetrofitManager2
import com.yannis.baselib.net.RunOn
import com.yannis.mayalisten.bean.ConcreteRankListBean
import com.yannis.mayalisten.net.MaYaApi
import io.reactivex.Observer

class DepositoryConcreteRankList: BaseDepository {


    fun  requestConcreteRankList(
        categoryId: Int,
        clusterType: Int,
        pageId: Int,
        rankingListId: Int,
        observer: Observer<BaseResultBean<ConcreteRankListBean>>
    ) {
      RetrofitManager2.getInstance().getApi(MaYaApi::class.java).getConcreteRankList(
            categoryId,
            clusterType,
            pageId,
            2,
            rankingListId
        ).compose(RunOn<BaseResultBean<ConcreteRankListBean>>())
          .subscribe(observer)

    }

}