package com.msc.libcoremodel.datamodel.http

import com.msc.libcoremodel.BuildConfig
import com.msc.libcoremodel.datamodel.http.ApiClient.initService
import com.msc.libcoremodel.datamodel.http.service.EyepetizerDataService

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by dxx on 2017/11/8.
 */

object ApiClient {

    /**
     * 获取指定数据类型
     * @return
     */
    val eyepetizerDataService: EyepetizerDataService
        get() {
            val eyepetizerDataService = initService(ApiConstants.baseUrl,
                    EyepetizerDataService::class.java)
            return initService(ApiConstants.baseUrl,
                    EyepetizerDataService::class.java)
        }

    /**单例retrofit */
    private lateinit var retrofitInstance: Retrofit

    /**单例OkHttpClient */
    private lateinit var okHttpClientInstance: OkHttpClient

    /**
     * 获得想要的 retrofit service
     * @param baseUrl  数据请求url
     * @param clazz    想要的 retrofit service 接口，Retrofit会代理生成对应的实体类
     * @param <T>      api service
     * @return
    </T> */
    private fun <T> initService(baseUrl: String, clazz: Class<T>): T {
        return getRetrofitInstance().create(clazz)
    }

    private fun getRetrofitInstance(): Retrofit {
        if (retrofitInstance == null) {
            synchronized(ApiClient::class.java) {
                if (retrofitInstance == null) {
                    retrofitInstance = Retrofit.Builder()
                            .baseUrl(ApiConstants.baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .client(getOkHttpClientInstance())
                            .build()
                }
            }
        }
        return retrofitInstance
    }

    private fun getOkHttpClientInstance(): OkHttpClient {
        if (okHttpClientInstance == null) {
            synchronized(ApiClient::class.java) {
                if (okHttpClientInstance == null) {
                    val builder = OkHttpClient().newBuilder()
                    if (BuildConfig.DEBUG) {
                        val httpLoggingInterceptor = HttpLoggingInterceptor()
                        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                        builder.addInterceptor(httpLoggingInterceptor)
                    }
                    okHttpClientInstance = builder.build()
                }
            }
        }
        return okHttpClientInstance
    }

}
