package com.msc.libcoremodel.datamodel.http.service;

import com.msc.libcoremodel.datamodel.http.entities.DiscoveryData;
import com.msc.libcoremodel.datamodel.http.entities.FollowData;
import com.msc.libcoremodel.datamodel.http.entities.TabsSelectedData;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface EyepetizerDataService {

    @GET("tabs/selected")
    Observable<TabsSelectedData> getTabsSelectedData();

    @GET("discovery")
    Observable<DiscoveryData> getDiscoveryData();

    @GET("tabs/follow")
    Observable<FollowData> getFollowData();

}
