package com.msc.libcoremodel.datamodel.http.service;

import com.msc.libcoremodel.datamodel.http.entities.AllRecData;
import com.msc.libcoremodel.datamodel.http.entities.DiscoveryData;
import com.msc.libcoremodel.datamodel.http.entities.FollowData;
import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface EyepetizerDataService {

    @GET("tabs/selected")
    Observable<TabsSelectedData> getTabsSelectedData();


//    http://baobab.kaiyanapp.com/api/v5/index/tab/allRec?page=0&udid=5ab5bd3e87e04215bf7820e58576aa192784ca51&vc=341&vn=3.19&deviceModel=MI%203W&first_channel=eyepetizer_xiaomi_market&last_channel=eyepetizer_xiaomi_market&system_version_code=23
    @GET("index/tab/allRec")
    Observable<AllRecData> getAllRecData(@Query("page") String page,
                                         @Query("udid") String udid,
                                         @Query("vc") String vc,
                                         @Query("vn") String vn,
                                         @Query("deviceModel") String deviceModel,
                                         @Query("first_channel") String first_channel,
                                         @Query("last_channel") String last_channel,
                                         @Query("system_version_code") String system_version_code);

    @GET
    Observable<AllRecData> getMoreRecData(@Url String url);

    @GET("discovery")
    Observable<DiscoveryData> getDiscoveryData();

    @GET("tabs/follow")
    Observable<FollowData> getFollowData();

}
