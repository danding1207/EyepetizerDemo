package com.msc.eyepetizer

import android.content.Context
import android.support.multidex.MultiDex
import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.BaseApplication
import com.msc.libcommon.util.Utils
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.tencent.tinker.loader.app.ApplicationLike
import com.tinkerpatch.sdk.TinkerPatch
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike
import com.meituan.android.walle.WalleChannelReader


class EyepetizerApplication : BaseApplication() {

    private var tinkerApplicationLike: ApplicationLike? = null

    override fun onCreate() {
        super.onCreate()
        initTinkerPatch()
        Utils.init(this)
        if (Utils.isAppDebug()) {
            //开启InstantRun之后，一定要在ARouter.init之前调用openDebug
            ARouter.openDebug()
            ARouter.openLog()
        }
        ARouter.init(this)

         val strategy = UserStrategy(this)

        //...在这里设置strategy的属性，在bugly初始化时传入
        strategy.appVersion = BuildConfig.VERSION_NAME //App的版本
//        strategy.appChannel = "myChannel"; //设置渠道
        strategy.appPackageName = BuildConfig.APPLICATION_ID //App的包名

        CrashReport.initCrashReport(this, BuildConfig.BUGLY_APPID, true, strategy)
        CrashReport.setUserId(Utils.getUDID()) //该用户本次启动后的异常日志用户ID都将是9 527

        val channel = WalleChannelReader.getChannel(this.applicationContext)
        Logger.i("Current channel is ： $channel")

    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        //you must install multiDex whatever tinker is installed!
        MultiDex.install(base)
    }

    /**
     * 我们需要确保至少对主进程跟patch进程初始化 TinkerPatch
     */
    private fun initTinkerPatch() {
        // 我们可以从这里获得Tinker加载过程的信息
        if (BuildConfig.TINKER_ENABLE) {
            tinkerApplicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike()
            // 初始化TinkerPatch SDK
            TinkerPatch.init(tinkerApplicationLike)
                    .reflectPatchLibrary()
                    .setPatchRollbackOnScreenOff(true)
                    .setPatchRestartOnSrceenOff(true)
                    .setFetchPatchIntervalByHours(1)

            // 获取当前的补丁版本
            Logger.i("Current patch version is " + TinkerPatch.with().patchVersion!!)

            // fetchPatchUpdateAndPollWithInterval 与 fetchPatchUpdate(false)
            // 不同的是，会通过handler的方式去轮询
            TinkerPatch.with().fetchPatchUpdateAndPollWithInterval()
        }
    }

}