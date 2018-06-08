package com.msc.libcoremodel.datamodel.http.utils

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Network
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Build.VERSION_CODES.LOLLIPOP
import android.os.Build.VERSION
import android.os.Build.VERSION.SDK_INT



object NetUtils {

    val DISCONNECTED = 0
    val WIFI_CONNECTED = 1
    val ETHERNET_CONNECTED = 2

    fun getNetConnStatus(context: Context?): Int {
        if (context == null) {
            return DISCONNECTED
        }

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        var wifiNetInfo:NetworkInfo  = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //新版本调用方法获取网络状态
//            val network = connectivityManager.getNetworkForType(ConnectivityManager.TYPE_WIFI)
//            connectivityManager.getNetworkInfo(network)
//        } else {
//            //旧版本调用方法获取网络状态
//            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//        }

        var wifiNetInfo:NetworkInfo  = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)

        var status = DISCONNECTED

        if (wifiNetInfo != null && wifiNetInfo.isAvailable && wifiNetInfo.isConnected) {
            status = WIFI_CONNECTED
        } else {
            val networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET)
            if (networkInfo != null && networkInfo.isAvailable && networkInfo.isConnected) {
                status = ETHERNET_CONNECTED
            }
        }

        return status
    }

    /**
     * 当前网络是否已连接
     */
    fun isNetConnected(context: Context?): Boolean {
        if (context == null) {
            return false
        }

        try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                // 获取网络连接管理的对象
                val infos = connectivity.allNetworkInfo
                if (infos != null) {
                    for (info in infos) {
                        if (info != null && info.isConnected) {
                            // 判断当前网络是否已经连接
                            if (info.state == NetworkInfo.State.CONNECTED) {
                                return true
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }

    /**
     * 当前网络是否已连接
     */
    fun netConnected(context: Context?): LiveData<Boolean> {
        val isNetConnect = MutableLiveData<Boolean>()
        if (context == null) {
            isNetConnect.value = false
            return isNetConnect
        }

        try {
            val connectivity = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivity != null) {
                // 获取网络连接管理的对象
                val infos = connectivity.allNetworkInfo
                if (infos != null) {
                    for (info in infos) {
                        if (info != null && info.isConnected) {
                            // 判断当前网络是否已经连接
                            if (info.state == NetworkInfo.State.CONNECTED) {
                                isNetConnect.value = true
                                return isNetConnect
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        isNetConnect.value = false
        return isNetConnect
    }

}