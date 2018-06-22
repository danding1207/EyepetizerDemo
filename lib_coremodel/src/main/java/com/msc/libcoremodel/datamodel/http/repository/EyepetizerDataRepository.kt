package com.msc.libcoremodel.datamodel.http.repository

import com.msc.libcoremodel.datamodel.http.ApiClient
import com.msc.libcoremodel.datamodel.http.entities.*

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
    val followDataRepository: Observable<FollowData>
        get() {

            val observableForGetFollowDataRepositoryFromNetWork = ApiClient
                    .eyepetizerDataService.followData

            return ApiClient
                    .eyepetizerDataService.followData
        }

    //可以操作Observable来筛选网络或者是本地数据
    fun getAllRecDataRepository(page: String, udid: String, vc: String,
                                vn: String, deviceModel: String, first_channel: String,
                                last_channel: String, system_version_code: String): Observable<AllRecData> {
        return ApiClient
                .eyepetizerDataService.getAllRecData(page, udid, vc,
                vn, deviceModel, first_channel,
                last_channel, system_version_code)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getMoreRecDataRepository(nextPageUrl: String): Observable<AllRecData> {
        return ApiClient
                .eyepetizerDataService.getMoreRecData(nextPageUrl)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getFeedDataRepository(udid: String, vc: String,
                              vn: String, deviceModel: String, first_channel: String,
                              last_channel: String, system_version_code: String): Observable<FeedData> {
        return ApiClient
                .eyepetizerDataService.getFeedData(udid, vc,
                vn, deviceModel, first_channel,
                last_channel, system_version_code)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getMoreFeedDataRepository(nextPageUrl: String): Observable<FeedData> {
        return ApiClient
                .eyepetizerDataService.getMoreFeedData(nextPageUrl)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getDiscoveryDataRepository(udid: String, vc: String,
                                   vn: String, deviceModel: String, first_channel: String,
                                   last_channel: String, system_version_code: String): Observable<DiscoveryData> {
        return ApiClient
                .eyepetizerDataService.getDiscoveryData(udid, vc,
                vn, deviceModel, first_channel,
                last_channel, system_version_code)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getMoreDiscoveryDataRepository(nextPageUrl: String): Observable<DiscoveryData> {
        return ApiClient
                .eyepetizerDataService.getMoreDiscoveryData(nextPageUrl)
    }


    //可以操作Observable来筛选网络或者是本地数据
    fun getMessagesDataRepository(udid: String, vc: String,
                                   vn: String, deviceModel: String, first_channel: String,
                                   last_channel: String, system_version_code: String): Observable<MessagesData> {
        return ApiClient
                .eyepetizerDataService.getMessagesData(udid, vc,
                vn, deviceModel, first_channel,
                last_channel, system_version_code)
    }

    //可以操作Observable来筛选网络或者是本地数据
    fun getMoreMessagesDataRepository(nextPageUrl: String): Observable<MessagesData> {
        return ApiClient
                .eyepetizerDataService.getMoreMessagesData(nextPageUrl)
    }







}
