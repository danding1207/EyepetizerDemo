package com.msc.libcoremodel.datamodel.http.repository

import com.msc.libcoremodel.datamodel.http.ApiClient
import com.msc.libcoremodel.datamodel.http.entities.DiscoveryData
import com.msc.libcoremodel.datamodel.http.entities.FollowData
import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData

import io.reactivex.Observable

object EyepetizerDataRepository {


    //可以操作Observable来筛选网络或者是本地数据
    val tabsSelectedDataRepository: Observable<TabsSelectedData>
        get() {
            val observableForGetTabsSelectedDataFromNetWork = ApiClient
                    .eyepetizerDataService.tabsSelectedData

            return ApiClient
                    .eyepetizerDataService.tabsSelectedData
        }

    //可以操作Observable来筛选网络或者是本地数据
    val discoveryDataRepository: Observable<DiscoveryData>
        get() {

            val observableForGetDiscoveryDataRepositoryFromNetWork = ApiClient
                    .eyepetizerDataService.discoveryData

            return ApiClient
                    .eyepetizerDataService.discoveryData
        }

    //可以操作Observable来筛选网络或者是本地数据
    val followDataRepository: Observable<FollowData>
        get() {

            val observableForGetFollowDataRepositoryFromNetWork = ApiClient
                    .eyepetizerDataService.followData

            return ApiClient
                    .eyepetizerDataService.followData
        }

}
