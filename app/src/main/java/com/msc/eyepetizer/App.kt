package com.msc.eyepetizer

import com.alibaba.android.arouter.launcher.ARouter
import com.msc.libcommon.base.BaseApplication
import com.msc.libcommon.util.Utils
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy


class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
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

        CrashReport.initCrashReport(this, BuildConfig.BUGLY_APPID, true, strategy);
        CrashReport.setUserId(Utils.getUDID()) //该用户本次启动后的异常日志用户ID都将是9 527

    }

}